package org.example.utils.order;

import org.example.utils.AbstractUtils;
import org.example.pojo.createorder.CreateOrderRequest;
import org.example.pojo.createorder.CreateOrderResponse;
import org.example.pojo.getorder.GetOrderResponse;

import static io.restassured.RestAssured.given;

public class OrderUtils extends AbstractUtils {

    public static int getOrderId(int track) {
        GetOrderResponse response =
                given()
                        .baseUri("https://qa-scooter.praktikum-services.ru/")
                        .queryParam("t", track)
                        .get("/api/v1/orders/track/")
                        .then()
                        .extract()
                        .body()
                        .as(GetOrderResponse.class);
        return response.getOrder().getId();
    }

    public static int postOrder(CreateOrderRequest request) {
        CreateOrderResponse response =
        given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract()
                .body()
                .as(CreateOrderResponse.class);
        return response.getTrack();
    }
}
