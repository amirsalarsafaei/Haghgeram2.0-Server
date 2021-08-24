package com.SalarJavaDevGroup.util;

import java.util.Random;

public class RandomGenerator {
    public static int RandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public static int RandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
}
