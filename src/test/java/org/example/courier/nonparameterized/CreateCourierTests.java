package org.example.courier.nonparameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.utils.courier.CourierGenerator;
import org.example.utils.courier.CourierSteps;
import org.example.utils.courier.CourierUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class CreateCourierTests {
    private static final String BASE_PATH = "/api/v1/courier";
    private CreateCourierRequest request;

    @After
    public void clean() {
        CourierUtils.deleteCourier(request);
    }

    @Test
    @DisplayName("Check status code and body have ok:true with valid request")
    public void createCourierCheckStatusCodeAndBody() {
        request = CourierGenerator.generateCourier();
        Response response = CourierSteps.sendPostRequest(request, BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_CREATED);
        CourierSteps.checkCreateCourierBodyOk(response, true);
    }

    @Test
    @DisplayName("Check status code and body error message with while creating duplicate courier")
    public void createDuplicateCourierCheckStatusCodeAndBody() {
        request = CourierGenerator.generateCourier();
        CourierUtils.postCourier(request);
        Response response = CourierSteps.sendPostRequest(request, BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_CONFLICT);
        CourierSteps.checkCreateCourierErrorMessage(response, "Этот логин уже используется");
    }
}
