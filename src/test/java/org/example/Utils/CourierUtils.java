package org.example.Utils;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.courierlogin.CourierLoginRequest;
import org.example.pojo.courierlogin.CourierLoginResponse;
import org.example.pojo.createcourier.CreateCourierRequest;

import static io.restassured.RestAssured.given;

public class CourierUtils {

    private static final RequestSpecification REQUEST_SPECIFICATION =
            new RequestSpecBuilder()
                    .setContentType(ContentType.JSON)
                    .setBaseUri("https://qa-scooter.praktikum-services.ru/")
                    .build();

    public static void postCourier(CreateCourierRequest request) {
        given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post("/api/v1/courier");
    }

    public static void deleteCourier(CreateCourierRequest request) {
        if (request.getLogin() != null && request.getPassword() != null) {
            given()
                    .baseUri("https://qa-scooter.praktikum-services.ru/")
                    .delete("/api/v1/courier/" + getCourierId(request));
        }
    }

    public static String getCourierId(CreateCourierRequest request) {
        if (request.getLogin() != null && request.getPassword() != null) {
            CourierLoginRequest courierLoginRequest = new CourierLoginRequest(request.getLogin(), request.getPassword());
            CourierLoginResponse response =
                    given()
                            .spec(REQUEST_SPECIFICATION)
                            .body(courierLoginRequest)
                            .post("/api/v1/courier/login")
                            .body()
                            .as(CourierLoginResponse.class);
            return response.getId();
        } else {
            throw new IllegalArgumentException("Login or Password is null");
        }
    }

    public static CourierLoginRequest createCourierLoginFromRequest(CreateCourierRequest request) {
        return new CourierLoginRequest(request.getLogin(), request.getPassword());
    }

    public static CreateCourierRequest generateCourier() {
        return new CreateCourierRequest(RandomStringUtils.random(5, true, true),
                RandomStringUtils.randomNumeric(5),
                RandomStringUtils.random(5, true, false));
    }

    public static CreateCourierRequest generateCourierWithoutLogin() {
        CreateCourierRequest request = generateCourier();
        request.setLogin(null);
        return request;
    }

    public static CreateCourierRequest generateCourierWithoutPassword() {
        CreateCourierRequest request = generateCourier();
        request.setPassword(null);
        return request;
    }

    public static CreateCourierRequest generateCourierWithoutFirstName() {
        CreateCourierRequest request = generateCourier();
        request.setFirstName(null);
        return request;
    }

    public static CourierLoginRequest generateCourierLoginWithoutLogin() {
        return new CourierLoginRequest(null, RandomStringUtils.randomNumeric(5));
    }

    public static CourierLoginRequest generateCourierLoginWithoutPassword() {
        return new CourierLoginRequest(RandomStringUtils.random(5, true, true), null);
    }

    public static String generateRandomCourierId() {
        return RandomStringUtils.randomNumeric(6);
    }

    public static CourierLoginRequest generateCourierLoginWithWrongLogin(CreateCourierRequest request) {
        String login = request.getLogin();
        boolean equal = true;
        while (equal) {
            if (login.equals(request.getLogin())) {
                login = RandomStringUtils.random(5, true, true);
            } else {
                equal = false;
            }
        }
        return new CourierLoginRequest(login, request.getPassword());
    }

    public static CourierLoginRequest generateCourierLoginWithWrongPassword(CreateCourierRequest request) {
        String password = request.getPassword();
        boolean equal = true;
        while (equal) {
            if (password.equals(request.getPassword())) {
                password = RandomStringUtils.randomNumeric(5);
            } else {
                equal = false;
            }
        }
        return new CourierLoginRequest(request.getLogin(), password);
    }
}
