package com.tradeshield.dao;

import java.sql.*;import java.util.*;import com.tradeshield.db.Database;import com.tradeshield.model.*;import com.tradeshield.exception.StockException;
public class OrderDao {
   public int place(int userId,int batchId,int quantity) throws SQLException,StockException {
    try(Connection c=Database.connect()){
        c.setAutoCommit(false);
      try {
        double price;int remaining;
        try(PreparedStatement p=c.prepareStatement("SELECT p.price,b.quantity FROM batches b JOIN products p ON b.product_id=p.id WHERE b.id=? AND b.recalled=0")){
            p.setInt(1,batchId);
                    try(ResultSet r=p.executeQuery()){
                        if(!r.next())throw new StockException("Batch not found or recalled.");
                        price=r.getDouble(1);remaining=r.getInt(2);
                    }
                }
                if(quantity>remaining)throw new StockException("Only "+remaining+" items left.");
                try(PreparedStatement p=c.prepareStatement("UPDATE batches SET quantity=quantity-? WHERE id=? AND recalled=0 AND quantity>=?")){
                    p.setInt(1,quantity);p.setInt(2,batchId);p.setInt(3,quantity);
                    if(p.executeUpdate()!=1)throw new StockException("Not enough stock.");
                }
                int orderId;
                try(PreparedStatement p=c.prepareStatement("INSERT INTO orders(user_id,total) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS)){
                    p.setInt(1,userId);p.setDouble(2,quantity*price);p.executeUpdate();
                    try(ResultSet keys=p.getGeneratedKeys()){if(!keys.next())throw new SQLException("Cannot create order");orderId=keys.getInt(1);}
                }
                try(PreparedStatement p=c.prepareStatement("INSERT INTO order_items(order_id,batch_id,quantity,price) VALUES(?,?,?,?)")){
                    p.setInt(1,orderId);p.setInt(2,batchId);p.setInt(3,quantity);p.setDouble(4,price);p.executeUpdate();
                }
                c.commit();return orderId;
            }catch(SQLException|StockException|RuntimeException ex){c.rollback();throw ex;}
        }
    }
    public List<Order> forCustomer(int userId)throws SQLException{
        List<Order> list=new ArrayList<>();
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("SELECT id,user_id,total FROM orders WHERE user_id=? ORDER BY id DESC")){
            p.setInt(1,userId);try(ResultSet r=p.executeQuery()){
                while(r.next())list.add(new Order(r.getInt(1),r.getInt(2),r.getDouble(3)));
            }
        }return list;
    }
    public List<OrderItem> itemsForCustomer(int userId)throws SQLException{
        List<OrderItem> list=new ArrayList<>();
        String sql="SELECT i.id,i.order_id,i.batch_id,i.quantity,i.price FROM order_items i JOIN orders o ON i.order_id=o.id WHERE o.user_id=? ORDER BY i.id DESC";
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,userId);try(ResultSet r=p.executeQuery()){
                while(r.next())list.add(new OrderItem(r.getInt(1),r.getInt(2),r.getInt(3),r.getInt(4),r.getDouble(5)));
            }
        }return list;
    }
    public Invoice invoice(int orderId,int userId)throws SQLException{
        try(Connection c=Database.connect();PreparedStatement p=c.prepareStatement("SELECT total FROM orders WHERE id=? AND user_id=?")){
            p.setInt(1,orderId);p.setInt(2,userId);try(ResultSet r=p.executeQuery()){
                return r.next()?new Invoice(orderId,r.getDouble(1)):null;
            }
        }
    }
}
