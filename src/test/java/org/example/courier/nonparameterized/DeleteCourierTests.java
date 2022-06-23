package org.example.courier.nonparameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.Utils.Courier.CourierGenerator;
import org.example.Utils.Courier.CourierSteps;
import org.example.Utils.Courier.CourierUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;

public class DeleteCourierTests {
    private static final String BASE_PATH = "/api/v1/courier/";

    @Test
    @DisplayName("Check status code and body have ok:true while deleting existing courier")
    public void deleteCourierCheckStatusCodeAndBody() {
        CreateCourierRequest request = CourierGenerator.generateCourier();
        CourierUtils.postCourier(request);
        Response response = CourierSteps.sendDeleteRequest(CourierUtils.getCourierId(request), BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_OK);
        CourierSteps.checkDeleteCourierBodyOk(response, true);
    }

    @Test
    @DisplayName("Check status code and body error message without id in request")
    public void deleteCourierWithoutIdCheckStatusCodeAndBody() {
        Response response = CourierSteps.sendDeleteRequest(BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_BAD_REQUEST);
        CourierSteps.checkDeleteCourierErrorMessage(response, "Недостаточно данных для удаления курьера");
    }

    @Test
    @DisplayName("Check status code and body error message while deleting nonexistent courier")
    public void deleteCourierWithNonexistentIdCheckStatusCodeAndBody() {
        Response response = CourierSteps.sendDeleteRequest(CourierGenerator.generateRandomCourierId(), BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_NOT_FOUND);
        CourierSteps.checkDeleteCourierErrorMessage(response, "Курьера с таким id нет");
    }
}
