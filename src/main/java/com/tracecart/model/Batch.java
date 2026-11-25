package com.tracecart.model;

public class Batch {
    private int id, productId, quantity; private String code; private boolean recalled;
    public Batch(int id,int productId,String code,int quantity,boolean recalled){this.id=id;this.productId=productId;this.code=code;this.quantity=quantity;this.recalled=recalled;}
    public int getId(){return id;} public int getProductId(){return productId;} public String getCode(){return code;} public int getQuantity(){return quantity;}
    public boolean isRecalled(){return recalled;}
    public String toString(){return id+" | Product #"+productId+" | "+code+" | Stock: "+quantity+" | "+(recalled?"RECALLED":"ACTIVE");}
}
