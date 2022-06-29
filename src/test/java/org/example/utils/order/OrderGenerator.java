package org.example.utils.order;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.pojo.createorder.CreateOrderRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderGenerator {

    public static CreateOrderRequest generateOrder() {
        return new CreateOrderRequest(RandomStringUtils.random(5, true, false),
                RandomStringUtils.random(5, true, false),
                RandomStringUtils.random(10, true, false),
                RandomStringUtils.randomNumeric(1),
                "+7" + RandomStringUtils.randomNumeric(10),
                new Random().nextInt(6) + 1,
                "2022-08-07",
                RandomStringUtils.random(10, true, false),
                new ArrayList<>(List.of()));
    }

    public static int generateRandomOrderId() {
        return Integer.parseInt(RandomStringUtils.randomNumeric(6));
    }

    public static int generateRandomOrderTrack() {
        return Integer.parseInt(RandomStringUtils.randomNumeric(7));
    }
}
