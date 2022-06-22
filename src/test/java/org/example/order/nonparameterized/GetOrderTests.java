package org.example.order.nonparameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.example.Utils.OrderUtils;
import org.example.pojo.createorder.CreateOrderRequest;
import org.example.pojo.getorder.GetOrderResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrderTests {
    private static int track;

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
        CreateOrderRequest request = OrderUtils.generateOrder();
        track = OrderUtils.postOrder(request);
    }

    @Test
    @DisplayName("Check response code and body have order details with valid request")
    public void getOrderCheckCheckStatusCodeAndBody() {
        Response response = sendGetRequest(track);
        checkStatusCode(response, 200);
        checkOrderNotNull(response);
    }

    @Test
    @DisplayName("Check response code and body error message without tracking number in request")
    public void getOrderWithoutTrackingNumberCheckStatusCodeAndBody() {
        Response response = sendGetRequest();
        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для поиска");
    }

    @Test
    @DisplayName("Check response code and body error message with nonexistent tracking number in request")
    public void getOrderWithWrongTrackingNumberCheckStatusCodeAndBody() {
        Response response = sendGetRequest(OrderUtils.generateRandomOrderTrack());
        checkStatusCode(response, 404);
        checkErrorMessage(response, "Заказ не найден");
    }

    @Step("Send GET to /api/v1/orders/track/")
    public Response sendGetRequest(int track) {
        return given()
                .queryParam("t", track)
                .get("/api/v1/orders/track/");
    }

    @Step("Send GET to /api/v1/orders/track/")
    public Response sendGetRequest() {
        return given()
                .get("/api/v1/orders/track/");
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Check body error message")
    public void checkErrorMessage(Response response, String message) {
        GetOrderResponse orderResponse = response.as(GetOrderResponse.class);
        assertThat(orderResponse.getMessage(), equalTo(message));
    }

    @Step("Check that body returns order details")
    public void checkOrderNotNull(Response response) {
        GetOrderResponse orderResponse = response.as(GetOrderResponse.class);
        assertThat(orderResponse.getOrder(), notNullValue());
    }
}
