package org.example.courier.parameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.example.Utils.CourierUtils;
import org.example.pojo.courierlogin.CourierLoginRequest;
import org.example.pojo.courierlogin.CourierLoginResponse;
import org.example.pojo.createcourier.CreateCourierRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Enclosed.class)
public class CourierLoginParameterizedTests {

    abstract public static class SharedSetUp {
        protected static RequestSpecification spec;
        protected static final CreateCourierRequest CREATE_COURIER_REQUEST = CourierUtils.generateCourier();

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

        @Step("Send POST to /api/v1/courier/login")
        public Response sendPostRequest(CourierLoginRequest request, RequestSpecification spec) {
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

        @Step("Check body error message")
        public void checkErrorMessage(Response response, String message) {
            CourierLoginResponse loginResponse = response.as(CourierLoginResponse.class);
            assertThat(loginResponse.getMessage(), equalTo(message));
        }
    }


    @RunWith(Parameterized.class)
    public static class CourierLoginWithoutSomethingTest extends SharedSetUp {
        private final CourierLoginRequest request;

        public CourierLoginWithoutSomethingTest(CourierLoginRequest request) {
            this.request = request;
        }

        @Parameterized.Parameters
        public static Object[][] getTestData() {
            return new Object[][]{
                    {CourierUtils.generateCourierLoginWithoutLogin()},
                    {CourierUtils.generateCourierLoginWithoutPassword()}
            };
        }

        @Test
        @DisplayName("Check status code and body error message without login or password")
        public void CourierLoginWithoutSomethingCheckStatusCodeAndBody() {
            Response response = sendPostRequest(request, spec);
            checkStatusCode(response, 400);
            checkErrorMessage(response, "Недостаточно данных для входа");
        }
    }

    @RunWith(Parameterized.class)
    public static class CourierLoginWithSomethingWrongTest extends SharedSetUp {
        private final CourierLoginRequest request;

        public CourierLoginWithSomethingWrongTest(CourierLoginRequest request) {
            this.request = request;
        }

        @Parameterized.Parameters
        public static Object[][] getTestData() {
            return new Object[][]{
                    {CourierUtils.generateCourierLoginWithWrongLogin(CREATE_COURIER_REQUEST)},
                    {CourierUtils.generateCourierLoginWithWrongPassword(CREATE_COURIER_REQUEST)}
            };
        }

        @Test
        @DisplayName("Check status code and body error message with wrong login or password")
        public void CourierLoginWithSomethingWrongCheckStatusCodeAndBody() {
            Response response = sendPostRequest(request, spec);
            checkStatusCode(response, 404);
            checkErrorMessage(response, "Учетная запись не найдена");
        }
    }
}
