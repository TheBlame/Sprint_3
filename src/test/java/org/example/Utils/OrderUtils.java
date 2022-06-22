package org.example.Utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.createorder.CreateOrderRequest;
import org.example.pojo.createorder.CreateOrderResponse;
import org.example.pojo.getorder.GetOrderResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class OrderUtils {

    private static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .setBaseUri("https://qa-scooter.praktikum-services.ru/")
                    .build();

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

    public static CreateOrderRequest generateOrder() {
        return new CreateOrderRequest(RandomStringUtils.random(5, true, false),
        RandomStringUtils.random(5, true, false),
        RandomStringUtils.random(10, true, false),
        RandomStringUtils.randomNumeric(1),
        "+7" + RandomStringUtils.randomNumeric(10),
        new Random().nextInt(6) + 1,
        "2022-08-07",
        RandomStringUtils.random(10, true, false),
        new ArrayList<>(List.of()));
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

    public static int generateRandomOrderId() {
        return Integer.parseInt(RandomStringUtils.randomNumeric(6));
    }

    public static int generateRandomOrderTrack() {
        return Integer.parseInt(RandomStringUtils.randomNumeric(7));
    }
}
