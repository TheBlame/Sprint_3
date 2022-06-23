package org.example.order.nonparameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Utils.Order.OrderGenerator;
import org.example.Utils.Order.OrderSteps;
import org.example.Utils.Order.OrderUtils;
import org.example.pojo.createorder.CreateOrderRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class GetOrderTests {
    private static int track;
    private static final String BASE_PATH = "/api/v1/orders/track/";

    @BeforeClass
    public static void setUp() {
        CreateOrderRequest request = OrderGenerator.generateOrder();
        track = OrderUtils.postOrder(request);
    }

    @Test
    @DisplayName("Check response code and body have order details with valid request")
    public void getOrderCheckCheckStatusCodeAndBody() {
        Response response = OrderSteps.sendGetRequest(track, BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_OK);
        OrderSteps.checkOrderNotNull(response);
    }

    @Test
    @DisplayName("Check response code and body error message without tracking number in request")
    public void getOrderWithoutTrackingNumberCheckStatusCodeAndBody() {
        Response response = OrderSteps.sendGetRequest(BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_BAD_REQUEST);
        OrderSteps.checkErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Check response code and body error message with nonexistent tracking number in request")
    public void getOrderWithWrongTrackingNumberCheckStatusCodeAndBody() {
        Response response = OrderSteps.sendGetRequest(OrderGenerator.generateRandomOrderTrack(), BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_NOT_FOUND);
        OrderSteps.checkErrorMessage(response, "Заказ не найден");
    }
}
