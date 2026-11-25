package com.tradeshield.util;

public class Validator {
    public static void positive(int n){if(n<=0)throw new IllegalArgumentException("Quantity must be positive.");}
    public static void price(double n){if(!Double.isFinite(n)||n<=0)throw new IllegalArgumentException("Price must be positive.");}
    public static void notBlank(String s){if(s==null||s.trim().isEmpty())throw new IllegalArgumentException("Field cannot be empty.");}
}
