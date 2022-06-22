package org.example.order.nonparameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.example.Utils.CourierUtils;
import org.example.Utils.OrderUtils;
import org.example.pojo.acceptorder.AcceptOrderResponse;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.example.pojo.createorder.CreateOrderRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class AcceptOrderTests {
    private CreateCourierRequest courierRequest;
    private int track;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        courierRequest = CourierUtils.generateCourier();
        CourierUtils.postCourier(courierRequest);
        CreateOrderRequest orderRequest = OrderUtils.generateOrder();
        track = OrderUtils.postOrder(orderRequest);
    }
    @After
    public void clean() {
        CourierUtils.deleteCourier(courierRequest);
    }

    @Test
    @DisplayName("Check response code and body have ok:true with valid courierId and orderId in request")
    public void acceptOrderCheckStatusCodeAndBody() {
        Response response = sendPutRequest(CourierUtils.getCourierId(courierRequest), OrderUtils.getOrderId(track));
        checkStatusCode(response, 200);
        checkBodyOk(response, true);
    }

    @Test
    @DisplayName("Check response code and body error message without courierId in request")
    public void acceptOrderWithoutCourierIdCheckStatusCodeAndBody() {
        Response response = sendPutRequest(OrderUtils.getOrderId(track));
        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Check response code and body error message with nonexistent courierId in request")
    public void acceptOrderWithWrongCourierIdCheckStatusCodeAndBody() {
        Response response = sendPutRequest(CourierUtils.generateRandomCourierId(), OrderUtils.getOrderId(track));
        checkStatusCode(response, 404);
        checkErrorMessage(response, "Курьера с таким id не существует");
    }

    @Test
    @DisplayName("Check response code and body error message without orderId in request")
    public void acceptOrderWithoutOrderIdCheckStatusCodeAndBody() {
        Response response = sendPutRequest(CourierUtils.getCourierId(courierRequest));
        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Check response code and body error message with nonexistent orderId in request")
    public void acceptOrderWithWrongOrderIdCheckStatusCodeAndBody() {
        Response response = sendPutRequest(CourierUtils.getCourierId(courierRequest), OrderUtils.generateRandomOrderId());
        checkStatusCode(response, 404);
        checkErrorMessage(response, "Заказа с таким id не существует");
    }

    @Step("Send PUT to /api/v1/orders/accept/")
    public Response sendPutRequest(String courierId, int orderId) {
        return given()
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/" + orderId);
    }

    @Step("Send PUT to /api/v1/orders/accept/")
    public Response sendPutRequest(String courierId) {
        return given()
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/");
    }

    @Step("Send PUT to /api/v1/orders/accept/")
    public Response sendPutRequest(int orderId) {
        return given()
                .put("/api/v1/orders/accept/" + orderId);
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check body error message")
    public void checkErrorMessage(Response response, String message) {
        AcceptOrderResponse acceptResponse = response.as(AcceptOrderResponse.class);
        assertThat(acceptResponse.getMessage(), equalTo(message));
    }

    @Step("Check body ok boolean")
    public void checkBodyOk(Response response, boolean b) {
        AcceptOrderResponse acceptResponse = response.as(AcceptOrderResponse.class);
        assertThat(acceptResponse.isOk(), equalTo(b));
    }
}
