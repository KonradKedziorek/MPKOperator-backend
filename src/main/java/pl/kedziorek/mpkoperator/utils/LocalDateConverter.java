package pl.kedziorek.mpkoperator.utils;

import java.time.LocalDate;

public class LocalDateConverter {

    public static LocalDate convertToLocalDate(String localDate){
        if(localDate!=null){
            return LocalDate.parse(localDate);
        }
        return null;
    }
}
