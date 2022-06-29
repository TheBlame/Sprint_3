package org.example.order.nonparameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.utils.courier.CourierGenerator;
import org.example.utils.courier.CourierUtils;
import org.example.utils.order.OrderGenerator;
import org.example.utils.order.OrderSteps;
import org.example.utils.order.OrderUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.example.pojo.createorder.CreateOrderRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class AcceptOrderTests {
    private CreateCourierRequest courierRequest;
    private int track;
    private static final String BASE_PATH = "/api/v1/orders/accept/";

    @Before
    public void setUp() {
        courierRequest = CourierGenerator.generateCourier();
        CourierUtils.postCourier(courierRequest);
        CreateOrderRequest orderRequest = OrderGenerator.generateOrder();
        track = OrderUtils.postOrder(orderRequest);
    }
    @After
    public void clean() {
        CourierUtils.deleteCourier(courierRequest);
    }

    @Test
    @DisplayName("Check response code and body have ok:true with valid courierId and orderId in request")
    public void acceptOrderCheckStatusCodeAndBody() {
        Response response = OrderSteps
                .sendPutRequest(CourierUtils.getCourierId(courierRequest), OrderUtils.getOrderId(track), BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_OK);
        OrderSteps.checkAcceptOrderBodyOk(response, true);
    }

    @Test
    @DisplayName("Check response code and body error message without courierId in request")
    public void acceptOrderWithoutCourierIdCheckStatusCodeAndBody() {
        Response response = OrderSteps.sendPutRequest(OrderUtils.getOrderId(track), BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_BAD_REQUEST);
        OrderSteps.checkAcceptOrderErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Check response code and body error message with nonexistent courierId in request")
    public void acceptOrderWithWrongCourierIdCheckStatusCodeAndBody() {
        Response response = OrderSteps
                .sendPutRequest(CourierGenerator.generateRandomCourierId(), OrderUtils.getOrderId(track), BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_NOT_FOUND);
        OrderSteps.checkAcceptOrderErrorMessage(response, "Курьера с таким id не существует");
    }

    @Test
    @DisplayName("Check response code and body error message without orderId in request")
    public void acceptOrderWithoutOrderIdCheckStatusCodeAndBody() {
        Response response = OrderSteps.sendPutRequest(CourierUtils.getCourierId(courierRequest), BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_BAD_REQUEST);
        OrderSteps.checkAcceptOrderErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Check response code and body error message with nonexistent orderId in request")
    public void acceptOrderWithWrongOrderIdCheckStatusCodeAndBody() {
        Response response = OrderSteps
                .sendPutRequest(CourierUtils.getCourierId(courierRequest), OrderGenerator.generateRandomOrderId(), BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_NOT_FOUND);
        OrderSteps.checkAcceptOrderErrorMessage(response, "Заказа с таким id не существует");
    }
}
