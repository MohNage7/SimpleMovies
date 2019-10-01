package com.mohnage7.movies.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private static final String NEW_FORMAT = "MMM dd, yyyy";
    private static final String OLD_FORMAT = "yyyy-mm-dd'T'hh:mm:ss'Z'";

    @NonNull
    public static String getDateWithNewFormat(String publishedAt) {
        String publishedAtFormatted = "";
        SimpleDateFormat oldFormat = new SimpleDateFormat(OLD_FORMAT);
        SimpleDateFormat newFormat = new SimpleDateFormat(NEW_FORMAT);
        try {
            Date date = oldFormat.parse(publishedAt);
            publishedAtFormatted = newFormat.format(date);
        } catch (ParseException e) {
            Log.e("DateParseFailure", e.getLocalizedMessage());
        }
        return publishedAtFormatted;
    }
}
