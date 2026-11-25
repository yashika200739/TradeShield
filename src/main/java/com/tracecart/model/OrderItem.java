package com.tracecart.model;

public class OrderItem {
    private int id, orderId, batchId, quantity; private double price;
    public OrderItem(int id,int orderId,int batchId,int quantity,double price){this.id=id;this.orderId=orderId;this.batchId=batchId;this.quantity=quantity;this.price=price;}
    public int getId(){return id;} public int getOrderId(){return orderId;}
    public String toString(){return "Item #"+id+" | Order #"+orderId+" | Batch #"+batchId+" | Qty "+quantity+" | Each Rs."+price;}
}
