package com.tradeshield.model;

/**
 * Represents one simulated stock order shown in the dashboard.
 *
 * <p>This class only stores order data in memory. Later stages can add
 * persistence and more detailed order states.</p>
 */
// Yashika[2510991179] - Order Model
public class Order {

    /** The two directions supported by a simulated order. */
    public enum OrderType {
        BUY,
        SELL
    }

    /** The only successful status used in the first Stage 2 increment. */
    public enum OrderStatus {
        ACCEPTED
    }

    private final String orderId;
    private final String stockSymbol;
    private final OrderType orderType;
    private final int quantity;
    private final double price;
    private final OrderStatus status;

    /**
     * Creates a simulated order after the dashboard has validated its values.
     */
    public Order(String orderId, String stockSymbol, OrderType orderType,
                 int quantity, double price, OrderStatus status) {
        this.orderId = orderId;
        this.stockSymbol = stockSymbol;
        this.orderType = orderType;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
