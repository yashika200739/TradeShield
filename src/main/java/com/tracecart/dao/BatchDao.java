package com.tracecart.dao;

import java.sql.*;import java.util.*;import com.tracecart.db.Database;import com.tracecart.model.Batch;
public class BatchDao {
    public List<Batch> all() throws SQLException{
        List<Batch> list=new ArrayList<>();
        try(Connection c=Database.connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM batches ORDER BY id")){
            while(r.next())list.add(new Batch(r.getInt("id"),r.getInt("product_id"),r.getString("code"),r.getInt("quantity"),r.getInt("recalled")==1));
        }return list;
    }
    public void add(int productId,String code,int quantity)throws SQLException{
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("INSERT INTO batches(product_id,code,quantity) VALUES(?,?,?)")){
            p.setInt(1,productId);p.setString(2,code);p.setInt(3,quantity);p.executeUpdate();
        }
    }
    public void restock(int id,int quantity)throws SQLException{
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("UPDATE batches SET quantity=quantity+? WHERE id=? AND recalled=0")){
            p.setInt(1,quantity);p.setInt(2,id);if(p.executeUpdate()==0)throw new SQLException("Batch not found or recalled.");
        }
    }
}
