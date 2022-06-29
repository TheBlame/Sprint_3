package org.example.utils.courier;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.utils.AbstractUtils;
import org.example.pojo.RequestBody;
import org.example.pojo.courierlogin.CourierLoginResponse;
import org.example.pojo.createcourier.CreateCourierResponse;
import org.example.pojo.deletecourier.DeleteCourierResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CourierSteps extends AbstractUtils {

    @Step("Send POST request")
    public static Response sendPostRequest(RequestBody request, String basePath) {
        return given()
                .spec(REQUEST_SPECIFICATION)
                .body(request)
                .when()
                .post(basePath);
    }

    @Step("Send DELETE request")
    public static Response sendDeleteRequest(String id, String basePath) {
        return given()
                .baseUri(BASE_URI)
                .delete(basePath + id);
    }

    @Step("Send DELETE request")
    public static Response sendDeleteRequest(String basePath) {
        return given()
                .baseUri(BASE_URI)
                .delete(basePath);
    }

    @Step("Check status code")
    public static void checkStatusCode(Response response, int i) {
        response.then().statusCode(i);
    }

    @Step("Check that body returns id")
    public static void checkIdNotNull(Response response) {
        CourierLoginResponse loginResponse = response.as(CourierLoginResponse.class);
        assertThat(loginResponse.getId(), notNullValue());
    }

    @Step("Check body error message")
    public static void checkCreateCourierErrorMessage(Response response, String message) {
        CreateCourierResponse createResponse = response.as(CreateCourierResponse.class);
        assertThat(createResponse.getMessage(), equalTo(message));
    }

    @Step("Check body error message")
    public static void checkCourierLoginErrorMessage(Response response, String message) {
        CourierLoginResponse loginResponse = response.as(CourierLoginResponse.class);
        assertThat(loginResponse.getMessage(), equalTo(message));
    }

    @Step("Check body ok boolean")
    public static void checkCreateCourierBodyOk(Response response, boolean b) {
        CreateCourierResponse createResponse = response.as(CreateCourierResponse.class);
        assertThat(createResponse.isOk(), equalTo(b));
    }

    @Step("Check body error message")
    public static void checkDeleteCourierErrorMessage(Response response, String message) {
        DeleteCourierResponse deleteResponse = response.as(DeleteCourierResponse.class);
        assertThat(deleteResponse.getMessage(), equalTo(message));
    }

    @Step("Check body ok boolean")
    public static void checkDeleteCourierBodyOk(Response response, boolean b) {
        DeleteCourierResponse deleteResponse = response.as(DeleteCourierResponse.class);
        assertThat(deleteResponse.isOk(), equalTo(b));
    }
}
