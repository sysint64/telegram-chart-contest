package ru.kabylin.andrey.telegramcontest.helpers;

import android.text.format.DateUtils;

import java.util.Date;

public class DateHelper {
    public static String humanizeDate(Date date, boolean showYear) {
        final int flags;

        if (!showYear) {
            flags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_NO_YEAR;
        } else {
            flags = DateUtils.FORMAT_ABBREV_MONTH;
        }

        return DateUtils.formatDateTime(null, date.getTime(), flags);
    }
}
