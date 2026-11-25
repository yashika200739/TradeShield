package com.tradeshield.model;

public class Invoice {
    private int orderId; private double total;
    public Invoice(int orderId,double total){this.orderId=orderId;this.total=total;}
    public int getOrderId(){return orderId;} public double getTotal(){return total;}
    public String toString(){return "Invoice for order #"+orderId+" | Total: Rs."+total;}
}
