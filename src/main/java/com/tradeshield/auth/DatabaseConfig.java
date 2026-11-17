package com.tradeshield.auth;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Reads database connection settings from the environment and opens JDBC connections. */
public final class DatabaseConfig {

    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/tradeshield";

    private DatabaseConfig() {
    }

    public static Connection openConnection() throws SQLException {
        String url = environmentOrDefault("TRADESHIELD_DB_URL", DEFAULT_URL);
        String username = System.getenv("TRADESHIELD_DB_USER");
        String password = System.getenv("TRADESHIELD_DB_PASSWORD");

        if (username == null || username.isBlank() || password == null) {
            throw new SQLException("Set TRADESHIELD_DB_USER and TRADESHIELD_DB_PASSWORD.");
        }

        return DriverManager.getConnection(url, username, password);
    }

    private static String environmentOrDefault(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
