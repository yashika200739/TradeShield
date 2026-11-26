package com.tradeshield.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Safe manual smoke test: the sample product insert is rolled back. */
public final class DatabaseConnectionTest {
    private DatabaseConnectionTest() {
    }

    public static void main(String[] args) {
        try (Connection connection = Database.connect();
             Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery("SELECT 1, DATABASE()")) {
                if (!result.next() || result.getInt(1) != 1) {
                    throw new SQLException("SELECT 1 returned no result.");
                }
                String databaseName = result.getString(2);
                if (!"tradeshield_db".equals(databaseName)) {
                    System.out.println("MySQL connection FAILED. The connected database is not tradeshield_db.");
                    System.exit(1);
                    return;
                }
                System.out.println("MySQL connection SUCCESS; database: " + databaseName + ".");
            }

        } catch (Exception e) {
            System.out.println("MySQL connection FAILED. Check .env, MySQL availability, and database setup.");
            System.exit(1);
            return;
        }

        try {
            Schema.create();
            System.out.println("Schema creation SUCCESS.");
        } catch (Exception e) {
            System.out.println("Schema creation FAILED. Check the MySQL database and CREATE TABLE permissions.");
            System.exit(1);
            return;
        }

        try (Connection connection = Database.connect(); Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery(
                     "SELECT COUNT(*) FROM information_schema.tables "
                             + "WHERE table_schema = DATABASE() AND table_name IN "
                             + "('users','products','batches','orders','order_items','claims','recalls')")) {
            if (!result.next() || result.getInt(1) != 7) {
                throw new SQLException("Required tables are missing.");
            }
            System.out.println("Required tables: SUCCESS (7 of 7 present).");
        } catch (Exception e) {
            System.out.println("Required tables: FAILED. Check the schema setup.");
            System.exit(1);
            return;
        }

        try (Connection connection = Database.connect()) {
            connection.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement insert = connection.prepareStatement(
                        "INSERT INTO products(name, category, price) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    insert.setString(1, "__TradeShield_connection_test__");
                    insert.setString(2, "__test__");
                    insert.setBigDecimal(3, new java.math.BigDecimal("1.00"));
                    insert.executeUpdate();
                    try (ResultSet keys = insert.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("No generated product key.");
                        }
                        id = keys.getInt(1);
                    }
                }

                try (PreparedStatement select = connection.prepareStatement(
                        "SELECT name FROM products WHERE id = ?")) {
                    select.setInt(1, id);
                    try (ResultSet row = select.executeQuery()) {
                        if (!row.next() || !"__TradeShield_connection_test__".equals(row.getString(1))) {
                            throw new SQLException("Inserted test row could not be fetched.");
                        }
                    }
                }
                connection.rollback();
                System.out.println("MySQL insert and fetch: SUCCESS (test transaction rolled back).");
            } catch (Exception e) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                    // Keep the user-facing error generic and do not print connection details.
                }
                System.out.println("MySQL storage check failed. Confirm database/schema setup and permissions.");
                System.exit(1);
            }
        } catch (SQLException e) {
            System.out.println("MySQL storage check failed. Confirm database/schema setup and permissions.");
            System.exit(1);
        }
    }
}
