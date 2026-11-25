package com.tracecart.dao;

import java.sql.*;import java.util.*;import com.tracecart.db.Database;import com.tracecart.model.Recall;
public class RecallDao {
    public void recall(int batchId,String reason)throws SQLException{
        try(Connection c=Database.connect()){
            c.setAutoCommit(false);
            try{
                try(PreparedStatement p=c.prepareStatement("UPDATE batches SET recalled=1 WHERE id=? AND recalled=0")){
                    p.setInt(1,batchId);if(p.executeUpdate()==0)throw new SQLException("Batch missing or already recalled.");
                }
                try(PreparedStatement p=c.prepareStatement("INSERT INTO recalls(batch_id,reason) VALUES(?,?)")){
                    p.setInt(1,batchId);p.setString(2,reason);p.executeUpdate();
                }
                c.commit();
            }catch(SQLException ex){c.rollback();throw ex;}
        }
    }
    public List<Recall> all()throws SQLException{
        List<Recall> list=new ArrayList<>();
        try(Connection c=Database.connect();Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT * FROM recalls ORDER BY id DESC")){
            while(r.next())list.add(new Recall(r.getInt("id"),r.getInt("batch_id"),r.getString("reason")));
        }return list;
    }
}
