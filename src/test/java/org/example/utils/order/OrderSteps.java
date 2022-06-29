package org.example.utils.order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.utils.AbstractUtils;
import org.example.pojo.RequestBody;
import org.example.pojo.acceptorder.AcceptOrderResponse;
import org.example.pojo.createorder.CreateOrderResponse;
import org.example.pojo.getorder.GetOrderResponse;
import org.example.pojo.getorderlist.GetOrderListResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class OrderSteps extends AbstractUtils {

    @Step("Send PUT request")
    public static Response sendPutRequest(String courierId, int orderId, String basePath) {
        return given()
                .baseUri(BASE_URI)
                .queryParam("courierId", courierId)
                .put(basePath + orderId);
    }

    @Step("Send PUT request")
    public static Response sendPutRequest(String courierId, String basePath) {
        return given()
                .baseUri(BASE_URI)
                .queryParam("courierId", courierId)
                .put(basePath);
    }

    @Step("Send PUT request")
    public static Response sendPutRequest(int orderId, String basePath) {
        return given()
                .baseUri(BASE_URI)
                .put(basePath + orderId);
    }

    @Step("Send GET request")
    public static Response sendGetRequest(String basePath) {
        return given()
                .baseUri(BASE_URI)
                .get(basePath);
    }

    @Step("Send GET request")
    public static Response sendGetRequest(int track, String basePath) {
        return given()
                .baseUri(BASE_URI)
                .queryParam("t", track)
                .get(basePath);
    }

    @Step("Send POST request")
    public static Response sendPostRequest(RequestBody request, String basePath) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post(basePath);
    }

    @Step("Check that body return track number")
    public static void checkTrackNotNull(Response response) {
        CreateOrderResponse createResponse = response.as(CreateOrderResponse.class);
        assertThat(createResponse.getTrack(), notNullValue());
    }

    @Step("Check that body returns order list")
    public static void checkOrdersNotNull(Response response) {
        GetOrderListResponse orderListResponse = response.as(GetOrderListResponse.class);
        assertThat(orderListResponse.getOrders(), notNullValue());
    }

    @Step("Check status code")
    public static void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check body error message")
    public static void checkAcceptOrderErrorMessage(Response response, String message) {
        AcceptOrderResponse acceptResponse = response.as(AcceptOrderResponse.class);
        assertThat(acceptResponse.getMessage(), equalTo(message));
    }

    @Step("Check body ok boolean")
    public static void checkAcceptOrderBodyOk(Response response, boolean b) {
        AcceptOrderResponse acceptResponse = response.as(AcceptOrderResponse.class);
        assertThat(acceptResponse.isOk(), equalTo(b));
    }

    @Step("Check body error message")
    public static void checkErrorMessage(Response response, String message) {
        GetOrderResponse orderResponse = response.as(GetOrderResponse.class);
        assertThat(orderResponse.getMessage(), equalTo(message));
    }

    @Step("Check that body returns order details")
    public static void checkOrderNotNull(Response response) {
        GetOrderResponse orderResponse = response.as(GetOrderResponse.class);
        assertThat(orderResponse.getOrder(), notNullValue());
    }
}
