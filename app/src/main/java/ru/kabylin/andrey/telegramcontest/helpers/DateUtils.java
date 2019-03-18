package ru.kabylin.andrey.telegramcontest.helpers;

import java.text.DateFormat;
import java.util.Date;

public class DateUtils {
    public static String humanizeDate(Date date) {
        final String format = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date);
        final int year = new Date().getYear();

        if (date.getYear() != year) {
            return format;
        } else {
            return format.split(",")[0];
        }
    }
}
