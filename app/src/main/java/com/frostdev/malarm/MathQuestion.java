package com.frostdev.malarm;

import java.util.Random;

public class MathQuestion {

    public static String generateQuestion(int age) {
        Random rand = new Random();

        if (age < 10) {
            int a = rand.nextInt(10);
            int b = rand.nextInt(10);
            return a + " + " + b + " = ?";
        } else if (age < 20) {
            int a = rand.nextInt(50) + 10;
            int b = rand.nextInt(50) + 10;
            return a + " * " + b + " = ?";
        } else {
            int a = rand.nextInt(100) + 50;
            int b = rand.nextInt(100) + 50;
            return a + " - " + b + " = ?";
        }
    }

}
