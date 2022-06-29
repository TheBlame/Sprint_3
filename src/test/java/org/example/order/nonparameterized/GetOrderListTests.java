package org.example.order.nonparameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Utils.Order.OrderSteps;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class GetOrderListTests {
    private static final String BASE_PATH = "/api/v1/orders";

    @Test
    @DisplayName("Check response code and body have list of orders with valid request")
    public void getOrderListCheckStatusCodeAndOrdersNotNull() {
        Response response = OrderSteps.sendGetRequest(BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_OK);
        OrderSteps.checkOrdersNotNull(response);
    }
}
