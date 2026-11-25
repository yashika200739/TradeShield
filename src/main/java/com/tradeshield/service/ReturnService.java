package com.tradeshield.service;

import java.sql.SQLException;import java.util.List;import com.tradeshield.dao.ReturnDao;import com.tradeshield.model.ReturnRequest;import com.tradeshield.util.Validator;
public class ReturnService {
    private final ReturnDao dao=new ReturnDao();
    public void request(int itemId,int userId,String type,String reason)throws SQLException{
        Validator.notBlank(reason);if(!type.equals("RETURN")&&!type.equals("REPLACEMENT"))throw new IllegalArgumentException("Invalid claim type.");
        dao.create(itemId,userId,type,reason);
    }
    public List<ReturnRequest> all()throws SQLException{return dao.all();}
    public List<ReturnRequest> mine(int id)throws SQLException{return dao.forCustomer(id);}
    public void review(int id,boolean approve)throws SQLException{dao.review(id,approve?"APPROVED":"REJECTED");}
}
