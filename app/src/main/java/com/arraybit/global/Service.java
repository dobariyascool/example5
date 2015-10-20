package com.arraybit.global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Service {

    public static String Url = "http://10.0.3.2:9852/Service.svc/";

    public static Boolean CheckNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static JSONObject HttpGetService(String url) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL Url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setDoInput(true);
            //conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);
            conn.connect();

            InputStream is = (InputStream) conn.getContent();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            return new JSONObject(responseStrBuilder.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject HttpPostService(String url, JSONStringer stringer) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL Url = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(20000);
            conn.connect();

            if (stringer != null) {
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(stringer.toString());
                wr.flush();
                wr.close();
            }

            InputStream is = conn.getInputStream();
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder responseStrBuilder = new StringBuilder();

            String inputStr;
            while ((inputStr = streamReader.readLine()) != null) {
                responseStrBuilder.append(inputStr);
            }

            return new JSONObject(responseStrBuilder.toString());
        } catch (Exception e) {
            return null;
        }
    }
}
