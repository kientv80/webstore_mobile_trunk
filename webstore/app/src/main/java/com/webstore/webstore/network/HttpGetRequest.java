package com.webstore.webstore.network;

import android.os.AsyncTask;
import android.os.Build;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by LAP10572-local on 7/14/2016.
 */
public class HttpGetRequest extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        String urlString = params[0].toString();
        HttpRequestListener callback = (HttpRequestListener) params[1];
        HttpURLConnection httpURLConnection = null;
        URL url = null;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL(urlString);
            httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            httpURLConnection.setRequestProperty("Content-Encoding", "gzip");
            httpURLConnection.setDoInput(true);

            int status = httpURLConnection.getResponseCode();
            //InputStream in = httpURLConnection.getInputStream();
            //InputStreamReader isw = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            callback.onRecievedData(result.toString());
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
        }
        return result;
    }
}
