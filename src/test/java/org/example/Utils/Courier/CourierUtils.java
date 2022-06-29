package org.example.Utils.Courier;

import org.example.Utils.AbstractUtils;
import org.example.pojo.courierlogin.CourierLoginRequest;
import org.example.pojo.courierlogin.CourierLoginResponse;
import org.example.pojo.createcourier.CreateCourierRequest;

import static io.restassured.RestAssured.given;

public class CourierUtils extends AbstractUtils {

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
                    .baseUri(BASE_URI)
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
}
