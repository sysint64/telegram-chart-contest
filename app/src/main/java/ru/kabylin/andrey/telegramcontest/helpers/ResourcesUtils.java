package ru.kabylin.andrey.telegramcontest.helpers;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.RawRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ResourcesUtils {
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

    public static String readTextAsset(AssetManager assetManager, String fileName) throws IOException {
        InputStream inputStream = assetManager.open(fileName);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        final StringBuilder json = new StringBuilder();

        while (line != null) {
            json.append(line);
            line = reader.readLine();
        }

        return json.toString();
    }

    public static boolean isAssetExists(AssetManager assetManager, String pathInAssetsDir) {
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open(pathInAssetsDir);
            return true;
        }  catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
