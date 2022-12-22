package com.example.asm3.base.networking.api;

import com.example.asm3.config.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ApiService {
    private String baseUrl;
    private HttpURLConnection httpURLConnection;

    public static String get = "GET";
    public static String post = "POST";

    public static String errorKey = "error";

    public ApiService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getJSON(String endPoint) {
        String json = "";
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
        } catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
        return json;
    }

    public String getJSON(String endPoint, String token) {
        String json = "";
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty(Constant.tokenHeader, token);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
        } catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
        return json;
    }

    public String postJSON(String endPoint, JSONObject jsonObject) {
        String json = "";
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(post);
            httpURLConnection.setRequestProperty(Constant.contentType, Constant.applicationJson + ";" + Constant.charsetUTF8);
            httpURLConnection.setRequestProperty(Constant.accept, Constant.applicationJson);
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(jsonObject.toString());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            json = stringBuilder.toString();

            dataOutputStream.flush();
            dataOutputStream.close();
        } catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch(ProtocolException protocolException) {
            protocolException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }

        return json;
    }
}
