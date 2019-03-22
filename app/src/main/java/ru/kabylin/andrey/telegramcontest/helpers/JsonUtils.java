package ru.kabylin.andrey.telegramcontest.helpers;

import android.content.res.Resources;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtils {
    public static String readResourceJson(Resources resource, @RawRes int id) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openRawResource(id)));
        String line = reader.readLine();
        final StringBuilder json = new StringBuilder();

        while (line != null) {
            json.append(line);
            line = reader.readLine();
        }

        return json.toString();
    }
}
