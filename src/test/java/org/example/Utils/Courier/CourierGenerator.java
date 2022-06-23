package org.example.Utils.Courier;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.courierlogin.CourierLoginRequest;
import org.example.pojo.createcourier.CreateCourierRequest;

public class CourierGenerator {

    public static CreateCourierRequest generateCourier() {
        return new CreateCourierRequest(RandomStringUtils.random(5, true, true),
                RandomStringUtils.randomNumeric(5),
                RandomStringUtils.random(5, true, false));
    }

    public static CreateCourierRequest generateCourierWithoutLogin() {
        CreateCourierRequest request = generateCourier();
        request.setLogin(null);
        return request;
    }

    public static CreateCourierRequest generateCourierWithoutPassword() {
        CreateCourierRequest request = generateCourier();
        request.setPassword(null);
        return request;
    }

    public static CreateCourierRequest generateCourierWithoutFirstName() {
        CreateCourierRequest request = generateCourier();
        request.setFirstName(null);
        return request;
    }

    public static CourierLoginRequest generateCourierLoginWithoutLogin() {
        return new CourierLoginRequest(null, RandomStringUtils.randomNumeric(5));
    }

    public static CourierLoginRequest generateCourierLoginWithoutPassword() {
        return new CourierLoginRequest(RandomStringUtils.random(5, true, true), null);
    }

    public static String generateRandomCourierId() {
        return RandomStringUtils.randomNumeric(6);
    }

    public static CourierLoginRequest generateCourierLoginWithWrongLogin(CreateCourierRequest request) {
        String login = request.getLogin();
        boolean equal = true;
        while (equal) {
            if (login.equals(request.getLogin())) {
                login = RandomStringUtils.random(5, true, true);
            } else {
                equal = false;
            }
        }
        return new CourierLoginRequest(login, request.getPassword());
    }

    public static CourierLoginRequest generateCourierLoginWithWrongPassword(CreateCourierRequest request) {
        String password = request.getPassword();
        boolean equal = true;
        while (equal) {
            if (password.equals(request.getPassword())) {
                password = RandomStringUtils.randomNumeric(5);
            } else {
                equal = false;
            }
        }
        return new CourierLoginRequest(request.getLogin(), password);
    }
}
