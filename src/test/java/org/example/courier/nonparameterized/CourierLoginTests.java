package org.example.courier.nonparameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.utils.courier.CourierGenerator;
import org.example.utils.courier.CourierSteps;
import org.example.utils.courier.CourierUtils;
import org.example.pojo.courierlogin.CourierLoginRequest;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CourierLoginTests {
    private static final CreateCourierRequest CREATE_COURIER_REQUEST = CourierGenerator.generateCourier();
    private static final String BASE_PATH = "/api/v1/courier/login";

    @BeforeClass
    public static void setUp() {
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
        Response response = CourierSteps.sendPostRequest(request, BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_OK);
        CourierSteps.checkIdNotNull(response);
    }
}
