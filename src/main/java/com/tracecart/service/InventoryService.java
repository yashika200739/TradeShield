package com.tracecart.service;

import java.sql.SQLException;import java.util.List;import com.tracecart.dao.BatchDao;import com.tracecart.model.Batch;import com.tracecart.util.Validator;
public class InventoryService {
    private final BatchDao dao=new BatchDao();
    public List<Batch> list()throws SQLException{return dao.all();}
    public void add(int productId,String code,int qty)throws SQLException{
        Validator.positive(productId);Validator.positive(qty);Validator.notBlank(code);dao.add(productId,code,qty);
    }
    public void restock(int id,int qty)throws SQLException{Validator.positive(qty);dao.restock(id,qty);}
}
