package com.example.mobileappprogrammingproject;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    private TaskCompleteListener<String> listener;
    public HttpAsyncTask(TaskCompleteListener listener){
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String jsonResponse = null;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuilder response = new StringBuilder();
            while ((output = br.readLine()) != null) {
                response.append(output);
            }

            conn.disconnect();

            jsonResponse = response.toString(); // JSON response

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (listener != null) {
            listener.onTaskComplete(result);
        }
    }
    public static String getRoute(String route){
        return String.format("http://%s:3000/%s","192.168.1.9", route);
    }

    public interface TaskCompleteListener<T> {
        void onTaskComplete(T result);
    }
}
