package pl.kedziorek.mpkoperator.utils;

import java.time.LocalDate;

public class IntegerConverter {

    public static Integer convertToInteger(String integer) {
        if (integer != null) {
            return Integer.parseInt(integer);
        } else {
            return null;
        }
    }
}
