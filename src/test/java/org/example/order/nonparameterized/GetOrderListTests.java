package org.example.order.nonparameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.example.pojo.getorderlist.GetOrderListResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetOrderListTests {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check response code and body have list of orders with valid request")
    public void getOrderListCheckStatusCodeAndOrdersNotNull() {
        Response response = sendGetRequest();
        checkStatusCode(response, 200);
        checkOrdersNotNull(response);
    }

    @Step("Send GET to /api/v1/orders")
    public Response sendGetRequest() {
        return given()
                .get("/api/v1/orders");
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Check that body returns order list")
    public void checkOrdersNotNull(Response response) {
        GetOrderListResponse orderListResponse = response.as(GetOrderListResponse.class);
        assertThat(orderListResponse.getOrders(), notNullValue());
    }
}
