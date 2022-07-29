package net.artemkv.journey3;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

final class HttpClient {
    private static final int CONNECTION_TIMEOUT = 30000; // 30 seconds
    private static final int READ_TIMEOUT = 30000; // 30 seconds

    private HttpClient() {}

    public static boolean trySend(String url, String json) {
        HttpURLConnection connection = null;
        OutputStream outStream = null;
        InputStream errorStream = null;
        InputStream resultStream = null;
        BufferedReader reader = null;
        try {
            byte[] data = json.getBytes(StandardCharsets.UTF_8);

            URL serverUrl = new URL(url);
            connection = (HttpURLConnection) serverUrl.openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));

            connection.setDoInput(true);

            // Write body
            outStream = connection.getOutputStream();
            outStream.write(data);
            outStream.flush();

            // Read response
            errorStream = connection.getErrorStream();
            if (errorStream != null) {
                resultStream = errorStream;
            } else {
                resultStream = connection.getInputStream();
            }
            reader = new BufferedReader(new InputStreamReader(resultStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.d("net.artemkv.journey3", line);
            }

            // TODO: extract error and throw exception
            int responseCode = connection.getResponseCode();
            if (responseCode >= 400) {
                return false;
            }
        } catch (IOException e) {
            Log.d("net.artemkv.journey3", e.getMessage(), e);
            return false;
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing reader", e);
            }

            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing output stream", e);
            }

            try {
                if (resultStream != null) {
                    resultStream.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing result stream", e);
            }

            try {
                if (errorStream != null) {
                    errorStream.close();
                }
            } catch (IOException e) {
                // Ignore
                Log.e("net.artemkv.journey3", "Error closing error stream", e);
            }

            if (connection != null) {
                connection.disconnect();
            }
        }
        return true;
    }
}
