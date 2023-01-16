package pl.kedziorek.mpkoperator.utils;

public class LongConverter {
    public static Long convertToLong(String longVariable) {
        if (longVariable != null) {
            return Long.parseLong(longVariable);
        } else {
            return null;
        }
    }

}
