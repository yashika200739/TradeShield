package com.tracecart.service;

import java.sql.SQLException;import java.util.List;import com.tracecart.dao.OrderDao;import com.tracecart.model.*;import com.tracecart.exception.StockException;import com.tracecart.util.Validator;
public class OrderService {
    private final OrderDao dao=new OrderDao();
    public int buy(int customerId,int batchId,int qty)throws SQLException,StockException{Validator.positive(qty);return dao.place(customerId,batchId,qty);}
    public List<Order> orders(int customerId)throws SQLException{return dao.forCustomer(customerId);}
    public List<OrderItem> items(int customerId)throws SQLException{return dao.itemsForCustomer(customerId);}
    public Invoice invoice(int orderId,int customerId)throws SQLException{return dao.invoice(orderId,customerId);}
}
