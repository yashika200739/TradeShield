package com.tracecart.dao;

import java.sql.*;import java.util.*;import com.tracecart.db.Database;import com.tracecart.model.ReturnRequest;
public class ReturnDao {
    public void create(int itemId,int userId,String type,String reason)throws SQLException{
        String sql="INSERT INTO claims(item_id,user_id,type,reason) SELECT i.id,?, ?,? FROM order_items i JOIN orders o ON i.order_id=o.id WHERE i.id=? AND o.user_id=?";
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,userId);p.setString(2,type);p.setString(3,reason);p.setInt(4,itemId);p.setInt(5,userId);
            if(p.executeUpdate()==0)throw new SQLException("Order item does not belong to you.");
        }
    }
    public List<ReturnRequest> all()throws SQLException{
        List<ReturnRequest> list=new ArrayList<>();
        try(Connection c=Database.connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM claims ORDER BY id DESC")){
            while(r.next())list.add(new ReturnRequest(r.getInt("id"),r.getInt("item_id"),r.getString("type"),r.getString("reason"),r.getString("status")));
        }return list;
    }
    public List<ReturnRequest> forCustomer(int userId)throws SQLException{
        List<ReturnRequest> list=new ArrayList<>();
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("SELECT * FROM claims WHERE user_id=? ORDER BY id DESC")){
            p.setInt(1,userId);try(ResultSet r=p.executeQuery()){
                while(r.next())list.add(new ReturnRequest(r.getInt("id"),r.getInt("item_id"),r.getString("type"),r.getString("reason"),r.getString("status")));
            }
        }return list;
    }
    public void review(int id,String status)throws SQLException{
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("UPDATE claims SET status=? WHERE id=? AND status='PENDING'")){
            p.setString(1,status);p.setInt(2,id);if(p.executeUpdate()==0)throw new SQLException("Pending claim not found.");
        }
    }
}
