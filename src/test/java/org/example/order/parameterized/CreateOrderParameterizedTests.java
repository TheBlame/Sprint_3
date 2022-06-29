package org.example.order.parameterized;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.example.utils.order.OrderGenerator;
import org.example.utils.order.OrderSteps;
import org.example.pojo.createorder.CreateOrderRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateOrderParameterizedTests {
    private final List<String> color;
    private static final String BASE_PATH = "/api/v1/orders";

    public CreateOrderParameterizedTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Test data: {0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
            {(List.of("GREY"))},
            {(List.of("BLACK"))},
            {(List.of("GREY", "BLACK"))},
            {(List.of())}
        });
    }

    @Test
    @DisplayName("Check response code and body have tracking number with different colors in request")
    public void createOrderColorTestCheckStatusCodeAndBody() {
        CreateOrderRequest request = OrderGenerator.generateOrder();
        request.setColor(color);
        Response response = OrderSteps.sendPostRequest(request,BASE_PATH);
        OrderSteps.checkStatusCode(response, SC_CREATED);
        OrderSteps.checkTrackNotNull(response);
    }
}
