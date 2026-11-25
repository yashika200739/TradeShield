package com.tradeshield.model;

public class Product {
    private int id; private String name, category; private double price;
    public Product(int id,String name,String category,double price){this.id=id;this.name=name;this.category=category;this.price=price;}
    public int getId(){return id;} public String getName(){return name;} public double getPrice(){return price;}
    public String toString(){return id+" | "+name+" | "+category+" | Rs."+price;}
}
