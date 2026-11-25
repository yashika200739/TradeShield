package com.tracecart.dao;

import java.sql.*;import java.util.*;import com.tracecart.db.Database;import com.tracecart.model.Product;
public class ProductDao {
    public List<Product> all() throws SQLException {
        List<Product> list=new ArrayList<>();
        try(Connection c=Database.connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM products ORDER BY id")){
            while(r.next())list.add(new Product(r.getInt("id"),r.getString("name"),r.getString("category"),r.getDouble("price")));
        }return list;
    }
    public void add(String name,String category,double price)throws SQLException{
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("INSERT INTO products(name,category,price) VALUES(?,?,?)")){
            p.setString(1,name);p.setString(2,category);p.setDouble(3,price);p.executeUpdate();
        }
    }
}
