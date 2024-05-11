package org.example.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LineValidationUtils
{
    public static final String LINE_SPLITTER = " ";
    private static final String REGEX_ALPHA_NUMERIC = "[a-zA-Z0-9]+";

    public static boolean isAlphanumeric(final String str)
    {
        return str != null && str.matches(REGEX_ALPHA_NUMERIC);
    }

    public static LocalTime parseTime(String timeStr)
    {
        return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
