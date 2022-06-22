package org.example.courier.nonparameterized;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.example.Utils.CourierUtils;
import org.example.pojo.createcourier.CreateCourierRequest;
import org.example.pojo.deletecourier.DeleteCourierResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DeleteCourierTests {

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Check status code and body have ok:true while deleting existing courier")
    public void deleteCourierCheckStatusCodeAndBody() {
        CreateCourierRequest request = CourierUtils.generateCourier();
        CourierUtils.postCourier(request);
        Response response = sendDeleteRequest(CourierUtils.getCourierId(request));
        checkStatusCode(response, 200);
        checkBodyOk(response, true);
    }

    @Test
    @DisplayName("Check status code and body error message without id in request")
    public void deleteCourierWithoutIdCheckStatusCodeAndBody() {
        Response response = sendDeleteRequest("");
        checkStatusCode(response, 400);
        checkErrorMessage(response, "Недостаточно данных для удаления курьера");
    }

    @Test
    @DisplayName("Check status code and body error message while deleting nonexistent courier")
    public void deleteCourierWithNonexistentIdCheckStatusCodeAndBody() {
        Response response = sendDeleteRequest(CourierUtils.generateRandomCourierId());
        checkStatusCode(response, 404);
        checkErrorMessage(response, "Курьера с таким id нет");
    }

    @Step("Send DELETE to /api/v1/courier/")
    public Response sendDeleteRequest(String id) {
        return given()
                .delete("/api/v1/courier/" + id);
    }

    @Step("Check status code")
    public void checkStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Check body error message")
    public void checkErrorMessage(Response response, String message) {
        DeleteCourierResponse deleteResponse = response.as(DeleteCourierResponse.class);
        assertThat(deleteResponse.getMessage(), equalTo(message));
    }

    @Step("Check body ok boolean")
    public void checkBodyOk(Response response, boolean b) {
        DeleteCourierResponse deleteResponse = response.as(DeleteCourierResponse.class);
        assertThat(deleteResponse.isOk(), equalTo(b));
    }
}
