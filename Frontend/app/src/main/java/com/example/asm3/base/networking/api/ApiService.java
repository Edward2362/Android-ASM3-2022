package com.example.asm3.base.networking.api;

import static android.content.ContentValues.TAG;

import android.util.Log;

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
    public static String delete = "DELETE";

    public static String errorKey = "error";

    public ApiService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getJSON(String endPoint) {
        String json = "";
        Log.d(TAG, "getJSON: test ");
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            Log.d(TAG, "getJSON:  test in try " + httpURLConnection);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            Log.d(TAG, "getJSON:  test in try");
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            Log.d(TAG, "getJSON:  test in try bufferedReader " + bufferedReader);
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
            Log.d(TAG, "getJSON:  test in try json " + json);
        } catch(MalformedURLException malformedURLException) {
            Log.d(TAG, "getJSON:  test in 1 catch");
            malformedURLException.printStackTrace();
        } catch(IOException ioException) {
            Log.d(TAG, "getJSON:  test in 2 catch");
            ioException.printStackTrace();
        }
        return json;
    }

    public String getJSON(String endPoint, String token) {
        String json = "";
        Log.d("place", baseUrl + endPoint);
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

    public String postJSON(String endPoint, JSONObject jsonObject, String token) {
        String json = "";
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(post);
            httpURLConnection.setRequestProperty(Constant.contentType, Constant.applicationJson + ";" + Constant.charsetUTF8);
            httpURLConnection.setRequestProperty(Constant.accept, Constant.applicationJson);
            httpURLConnection.setRequestProperty(Constant.tokenHeader, token);
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
        } catch (Exception exception){
            exception.printStackTrace();
        }

        return json;
    }

    public String deleteJSON(String endPoint) {
        String json = "";
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(delete);
            httpURLConnection.setRequestProperty(Constant.contentType, Constant.applicationJson + ";" + Constant.charsetUTF8);
            httpURLConnection.setRequestProperty(Constant.accept, Constant.applicationJson);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            String line = "";

            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            json = stringBuilder.toString();
        } catch(MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch(ProtocolException protocolException) {
            protocolException.printStackTrace();
        } catch(IOException ioException) {
            ioException.printStackTrace();
        }
        return json;
    }

    public String deleteJSON(String endPoint, String token) {
        String json = "";
        try {
            URL url = new URL(baseUrl + endPoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(delete);
            httpURLConnection.setRequestProperty(Constant.contentType, Constant.applicationJson + ";" + Constant.charsetUTF8);
            httpURLConnection.setRequestProperty(Constant.accept, Constant.applicationJson);
            httpURLConnection.setRequestProperty(Constant.tokenHeader, token);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }


            json = stringBuilder.toString();
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
