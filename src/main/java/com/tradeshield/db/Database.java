package com.tradeshield.db;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Database {
    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

    private static String setting(String key) {
        String fromEnvironment = System.getenv(key);
        String value = fromEnvironment != null ? fromEnvironment : DOTENV.get(key);
        if (value == null || value.isBlank()
                || value.startsWith("your_")) {
            throw new IllegalStateException("Database configuration is incomplete. Set " + key + " in .env.");
        }
        return value.trim();
    }

    private static String jdbcUrl() {
        String host = setting("DB_HOST");
        String database = setting("DB_NAME");
        int port;
        try {
            port = Integer.parseInt(setting("DB_PORT"));
        } catch (NumberFormatException e) {
            throw new IllegalStateException("DB_PORT in .env must be a number.");
        }
        if (port < 1 || port > 65535) {
            throw new IllegalStateException("DB_PORT in .env is outside the valid range.");
        }
        return "jdbc:mysql://" + host + ":" + port + "/" + database + "?serverTimezone=UTC";
    }

    public static Connection connect() throws SQLException {
        String username = setting("DB_USERNAME");
        String password = setting("DB_PASSWORD");
        return DriverManager.getConnection(jdbcUrl(), username, password);
    }
}
