package com.tradeshield.service;

import java.sql.SQLException;import java.util.List;import com.tradeshield.dao.RecallDao;import com.tradeshield.model.Recall;import com.tradeshield.util.Validator;
public class RecallService {
    private final RecallDao dao=new RecallDao();
    public void recall(int id,String reason)throws SQLException{Validator.notBlank(reason);dao.recall(id,reason);}
    public List<Recall> list()throws SQLException{return dao.all();}
}
