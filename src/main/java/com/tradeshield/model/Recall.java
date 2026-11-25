package com.tradeshield.model;

public class Recall {
    private int id, batchId; private String reason;
    public Recall(int id,int batchId,String reason){this.id=id;this.batchId=batchId;this.reason=reason;}
    public String toString(){return "Recall #"+id+" | Batch #"+batchId+" | "+reason;}
}
