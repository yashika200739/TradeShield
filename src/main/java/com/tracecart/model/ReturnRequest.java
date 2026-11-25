package com.tracecart.model;

public class ReturnRequest {
    private int id, itemId; private String type, reason, status;
    public ReturnRequest(int id,int itemId,String type,String reason,String status){this.id=id;this.itemId=itemId;this.type=type;this.reason=reason;this.status=status;}
    public String toString(){return "Claim #"+id+" | Item #"+itemId+" | "+type+" | "+status+" | "+reason;}
}
