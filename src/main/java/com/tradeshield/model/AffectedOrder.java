package com.tradeshield.model;

public class AffectedOrder {
    private final int orderId;
    private final String customerName;
    private final String customerEmail;
    private final int quantity;

    public AffectedOrder(int orderId, String customerName, String customerEmail, int quantity) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "Order #" + orderId + " | Customer: " + customerName + " | "
                + customerEmail + " | Purchased: " + quantity;
    }
}
