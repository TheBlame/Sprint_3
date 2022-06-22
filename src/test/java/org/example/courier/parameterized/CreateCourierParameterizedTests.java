package org.example.courier.parameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.example.Utils.CourierUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.example.pojo.createcourier.CreateCourierResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Parameterized.class)
public class CreateCourierParameterizedTests {
    private final CreateCourierRequest request;
    private static RequestSpecification spec;

    public CreateCourierParameterizedTests(CreateCourierRequest request) {
        this.request = request;
    }

    @Parameterized.Parameters
    public static Object[][] getTestData() {
        return new Object[][]{
                {CourierUtils.generateCourierWithoutLogin()},
                {CourierUtils.generateCourierWithoutPassword()},
                {CourierUtils.generateCourierWithoutFirstName()}
        };
    }

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
    @DisplayName("Check status code and body error message without login, password or firstName")
    public void createCourierWithoutSomethingCheckStatusCodeAndBody() {
       Response response = sendPostRequest(request);
       checkStatusCode(response, 400);
       checkErrorMessage(response, "Недостаточно данных для создания учетной записи");
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
