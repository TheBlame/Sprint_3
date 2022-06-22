package org.example.courier.nonparameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.example.Utils.CourierUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.example.pojo.createcourier.CreateCourierResponse;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;


public class CreateCourierTests {
    private static RequestSpecification spec;
    private CreateCourierRequest request;

    @BeforeClass
    public static void setUp() {
        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://qa-scooter.praktikum-services.ru/")
                .build();
    }

    @After
    public void clean() {
        CourierUtils.deleteCourier(request);
    }

    @Test
    @DisplayName("Check status code and body have ok:true with valid request")
    public void createCourierCheckStatusCodeAndBody() {
        request = CourierUtils.generateCourier();
        Response response = sendPostRequest(request);
        checkStatusCode(response, 201);
        checkBodyOk(response, true);
    }

    @Test
    @DisplayName("Check status code and body error message with while creating duplicate courier")
    public void createDuplicateCourierCheckStatusCodeAndBody() {
        request = CourierUtils.generateCourier();
        Response response = sendPostRequest(request);
        checkStatusCode(response, 409);
        checkErrorMessage(response, "Этот логин уже используется");
    }

    @Step("Send POST to /api/v1/courier")
    public Response sendPostRequest(CreateCourierRequest request) {
        return given()
                .spec(spec)
                .body(request)
                .when()
                .post("/api/v1/courier");
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Check body error message")
    public void checkErrorMessage(Response response, String message) {
        CreateCourierResponse createResponse = response.as(CreateCourierResponse.class);
        assertThat(createResponse.getMessage(), equalTo(message));
    }

    @Step("Check body ok boolean")
    public void checkBodyOk(Response response, boolean b) {
        CreateCourierResponse createResponse = response.as(CreateCourierResponse.class);
        assertThat(createResponse.isOk(), equalTo(b));
    }
}
