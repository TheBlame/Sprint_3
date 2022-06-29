package org.example.courier.parameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.utils.courier.CourierGenerator;
import org.example.utils.courier.CourierSteps;
import org.example.utils.courier.CourierUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateCourierParameterizedTests {
    private final CreateCourierRequest request;
    private static final String BASE_PATH = "/api/v1/courier";

    public CreateCourierParameterizedTests(CreateCourierRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {CourierGenerator.generateCourierWithoutLogin()},
                {CourierGenerator.generateCourierWithoutPassword()},
                {CourierGenerator.generateCourierWithoutFirstName()}
        };
    }

    @After
    public void clean() {
        CourierUtils.deleteCourier(request);
    }

    @Test
    @DisplayName("Check status code and body error message without login, password or firstName")
    public void createCourierWithoutSomethingCheckStatusCodeAndBody() {
        Response response = CourierSteps.sendPostRequest(request, BASE_PATH);
        CourierSteps.checkStatusCode(response, SC_BAD_REQUEST);
        CourierSteps.checkCreateCourierErrorMessage(response, "Недостаточно данных для создания учетной записи");
    }
}
