package com.tradeshield.dao;

import java.sql.*; import com.tradeshield.db.Database; import com.tradeshield.model.User;
public class UserDao {
    public User login(String email,String password) throws SQLException {
        String sql="SELECT id,name,email,role FROM users WHERE email=? AND password=?";
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,email);p.setString(2,password);
            try(ResultSet r=p.executeQuery()){if(r.next())return new User(r.getInt(1),r.getString(2),r.getString(3),r.getString(4));}
        } return null;
    }
    public void register(String name,String email,String password) throws SQLException {
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("INSERT INTO users(name,email,password,role) VALUES(?,?,?,'CUSTOMER')")){
            p.setString(1,name);p.setString(2,email);p.setString(3,password);p.executeUpdate();
        }
    }
}
