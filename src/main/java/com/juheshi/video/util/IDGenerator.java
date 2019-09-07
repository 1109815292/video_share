package com.juheshi.video.util;

import java.util.Random;

public class IDGenerator {

    public static final String[] SOURCE = new String[]{
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String getRandom(int size) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        int index;
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                do {
                    index = r.nextInt(SOURCE.length);
                } while (index == 0);
            } else {
                index = r.nextInt(SOURCE.length);
            }
            sb.append(SOURCE[index]);
        }
        return sb.toString();
    }

}

