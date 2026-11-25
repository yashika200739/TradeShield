package com.tradeshield.db;

import java.sql.*;
public class Schema {
    public static void create() throws SQLException {
        try (Connection c = Database.connect(); Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT UNIQUE NOT NULL, password TEXT NOT NULL, role TEXT NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS products (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, category TEXT NOT NULL, price REAL NOT NULL CHECK(price > 0))");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS batches (id INTEGER PRIMARY KEY AUTOINCREMENT, product_id INTEGER NOT NULL REFERENCES products(id), code TEXT UNIQUE NOT NULL, quantity INTEGER NOT NULL CHECK(quantity >= 0), recalled INTEGER NOT NULL DEFAULT 0)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL REFERENCES users(id), total REAL NOT NULL, created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS order_items (id INTEGER PRIMARY KEY AUTOINCREMENT, order_id INTEGER NOT NULL REFERENCES orders(id), batch_id INTEGER NOT NULL REFERENCES batches(id), quantity INTEGER NOT NULL CHECK(quantity > 0), price REAL NOT NULL)");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS claims (id INTEGER PRIMARY KEY AUTOINCREMENT, item_id INTEGER NOT NULL REFERENCES order_items(id), user_id INTEGER NOT NULL REFERENCES users(id), type TEXT NOT NULL, reason TEXT NOT NULL, status TEXT NOT NULL DEFAULT 'PENDING')");
            s.executeUpdate("CREATE TABLE IF NOT EXISTS recalls (id INTEGER PRIMARY KEY AUTOINCREMENT, batch_id INTEGER UNIQUE NOT NULL REFERENCES batches(id), reason TEXT NOT NULL)");
            s.executeUpdate("INSERT OR IGNORE INTO users(id,name,email,password,role) VALUES(1,'Admin','admin@tradeshield.com','admin123','ADMIN')");
            try (ResultSet r = s.executeQuery("SELECT COUNT(*) FROM products")) {
                if (r.next() && r.getInt(1) == 0) {
                    s.executeUpdate("INSERT INTO products(name,category,price) VALUES('Laptop','Electronics',45000),('Headphones','Electronics',1500),('Notebook','Stationery',80)");
                    s.executeUpdate("INSERT INTO batches(product_id,code,quantity) VALUES(1,'LAP-A',10),(1,'LAP-B',8),(2,'HEAD-A',20),(3,'NOTE-A',50)");
                }
            }
        }
    }
}
