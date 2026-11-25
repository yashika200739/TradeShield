package com.tracecart.service;

import java.sql.SQLException;import java.util.List;import com.tracecart.dao.ProductDao;import com.tracecart.model.Product;import com.tracecart.util.Validator;
public class ProductService {
    private final ProductDao dao=new ProductDao();
    public List<Product> list()throws SQLException{return dao.all();}
    public void add(String name,String category,double price)throws SQLException{
        Validator.notBlank(name);Validator.notBlank(category);Validator.price(price);dao.add(name,category,price);
    }
}
