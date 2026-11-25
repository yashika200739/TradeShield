package com.tracecart.service;

import java.sql.SQLException;import java.util.List;import com.tracecart.dao.RecallDao;import com.tracecart.model.Recall;import com.tracecart.util.Validator;
public class RecallService {
    private final RecallDao dao=new RecallDao();
    public void recall(int id,String reason)throws SQLException{Validator.notBlank(reason);dao.recall(id,reason);}
    public List<Recall> list()throws SQLException{return dao.all();}
}
