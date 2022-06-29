package org.example.courier.parameterized;

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
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.*;

@RunWith(Enclosed.class)
public class CourierLoginParameterizedTests {

    abstract public static class SharedSetUp {
        protected static final String BASE_PATH = "/api/v1/courier/login";
        protected static final CreateCourierRequest CREATE_COURIER_REQUEST = CourierGenerator.generateCourier();

        @BeforeClass
        public static void setUp() {
            CourierUtils.postCourier(CREATE_COURIER_REQUEST);
        }

        @AfterClass
        public static void clean() {
            CourierUtils.deleteCourier(CREATE_COURIER_REQUEST);
        }
    }


    @RunWith(Parameterized.class)
    public static class CourierLoginWithoutSomethingTest extends SharedSetUp {
        private final CourierLoginRequest request;

        public CourierLoginWithoutSomethingTest(CourierLoginRequest request) {
            this.request = request;
        }

        @Parameterized.Parameters(name = "Test data: {0}")
        public static Object[][] getTestData() {
            return new Object[][]{
                    {CourierGenerator.generateCourierLoginWithoutLogin()},
                    {CourierGenerator.generateCourierLoginWithoutPassword()}
            };
        }

        @Test
        @DisplayName("Check status code and body error message without login or password")
        public void CourierLoginWithoutSomethingCheckStatusCodeAndBody() {
            Response response = CourierSteps.sendPostRequest(request, BASE_PATH);
            CourierSteps.checkStatusCode(response, SC_BAD_REQUEST);
            CourierSteps.checkCourierLoginErrorMessage(response, "Недостаточно данных для входа");
        }
    }

    @RunWith(Parameterized.class)
    public static class CourierLoginWithSomethingWrongTest extends SharedSetUp {
        private final CourierLoginRequest request;

        public CourierLoginWithSomethingWrongTest(CourierLoginRequest request) {
            this.request = request;
        }

        @Parameterized.Parameters(name = "Test data: {0}")
        public static Object[][] getTestData() {
            return new Object[][]{
                    {CourierGenerator.generateCourierLoginWithWrongLogin(CREATE_COURIER_REQUEST)},
                    {CourierGenerator.generateCourierLoginWithWrongPassword(CREATE_COURIER_REQUEST)}
            };
        }

        @Test
        @DisplayName("Check status code and body error message with wrong login or password")
        public void CourierLoginWithSomethingWrongCheckStatusCodeAndBody() {
            Response response = CourierSteps.sendPostRequest(request, BASE_PATH);
            CourierSteps.checkStatusCode(response, SC_NOT_FOUND);
            CourierSteps.checkCourierLoginErrorMessage(response, "Учетная запись не найдена");
        }
    }
}
