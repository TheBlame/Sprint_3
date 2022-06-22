package org.example.courier.nonparameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.*;
import org.example.Utils.CourierUtils;
import org.example.pojo.courierlogin.CourierLoginRequest;
import org.example.pojo.courierlogin.CourierLoginResponse;
import org.example.pojo.createcourier.CreateCourierRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class CourierLoginTests {
    private static RequestSpecification spec;
    private static final CreateCourierRequest CREATE_COURIER_REQUEST = CourierUtils.generateCourier();

    @BeforeClass
    public static void setUp() {
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://qa-scooter.praktikum-services.ru/")
                .build();
        CourierUtils.postCourier(CREATE_COURIER_REQUEST);
    }

    @AfterClass
    public static void clean() {
        CourierUtils.deleteCourier(CREATE_COURIER_REQUEST);
    }

    @Test
    @DisplayName("Check status code and body have id with valid login and password")
    public void courierLoginCheckStatusCodeAndBody() {
        CourierLoginRequest request = CourierUtils.createCourierLoginFromRequest(CREATE_COURIER_REQUEST);
        Response response = sendPostRequest(request);
        checkStatusCode(response, 200);
        checkIdNotNull(response);
    }

    @Step("Send POST to /api/v1/courier/login")
    public Response sendPostRequest(CourierLoginRequest request) {
        return given()
                .spec(spec)
                .body(request)
                .when()
                .post("/api/v1/courier/login");
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check that body returns id")
    public void checkIdNotNull(Response response) {
        CourierLoginResponse loginResponse = response.as(CourierLoginResponse.class);
        assertThat(loginResponse.getId(), notNullValue());
    }
}
