package com.tracecart.db;

import java.sql.*;
public class Database {
    private static final String URL = "jdbc:sqlite:data/tradeshield.db";
    public static Connection connect() throws SQLException {
        Connection c = DriverManager.getConnection(URL);
        try (Statement s = c.createStatement()) { s.execute("PRAGMA foreign_keys = ON"); }
        return c;
    }
}
