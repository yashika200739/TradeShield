package com.tracecart.service;

import java.sql.SQLException;import com.tracecart.dao.UserDao;import com.tracecart.model.User;import com.tracecart.util.Validator;
public class AuthService {
    private final UserDao dao=new UserDao();
    public User login(String email,String password)throws SQLException{return dao.login(email,password);}
    public void register(String name,String email,String password)throws SQLException{
        Validator.notBlank(name);Validator.notBlank(email);Validator.notBlank(password);dao.register(name,email,password);
    }
}
