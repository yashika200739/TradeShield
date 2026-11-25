package com.tracecart.service;

import java.sql.SQLException;import com.tracecart.model.Invoice;import com.tracecart.service.OrderService;import com.tracecart.util.InvoicePrinter;
public class BillService {
    private final OrderService orders=new OrderService();
    public void print(int orderId,int customerId)throws SQLException{
        Invoice invoice=orders.invoice(orderId,customerId);
        if(invoice==null)System.out.println("Invoice not found for your account.");else InvoicePrinter.print(invoice);
    }
}
