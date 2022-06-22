package org.example.order.parameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.example.Utils.OrderUtils;
import org.example.pojo.createorder.CreateOrderRequest;
import org.example.pojo.createorder.CreateOrderResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTests {
    private static RequestSpecification spec;
    private final List<String> color;

    public CreateOrderParameterizedTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
            {(List.of("GREY"))},
            {(List.of("BLACK"))},
            {(List.of("GREY", "BLACK"))},
            {(List.of())}
        });
    }

    @BeforeClass
    public static void setUp() {
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://qa-scooter.praktikum-services.ru/")
                .build();
    }

    @Test
    @DisplayName("Check response code and body have tracking number with different colors in request")
    public void createOrderColorTestCheckStatusCodeAndBody() {
        CreateOrderRequest request = OrderUtils.generateOrder();
        request.setColor(color);
        Response response = sendPostRequest(request);
        checkStatusCode(response, 201);
        checkTrackNotNull(response);
    }

    @Step("Send POST to /api/v1/orders")
    public Response sendPostRequest(CreateOrderRequest request) {
        return given()
                .spec(spec)
                .body(request)
                .when()
                .post("/api/v1/orders");
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Check that body return track number")
    public void checkTrackNotNull(Response response) {
        CreateOrderResponse createResponse = response.as(CreateOrderResponse.class);
        assertThat(createResponse.getTrack(), notNullValue());
    }
}
