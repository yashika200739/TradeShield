package com.tracecart.model;

public class Order {
    private int id, customerId; private double total;
    public Order(int id,int customerId,double total){this.id=id;this.customerId=customerId;this.total=total;}
    public int getId(){return id;} public double getTotal(){return total;}
    public String toString(){return "Order #"+id+" | Customer #"+customerId+" | Rs."+total;}
}
