package org.example.pojo.getorder;

import org.example.pojo.getorder.nestedclasses.Order;

public class GetOrderResponse {
    private Order order;
    private String message;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
