package com.webstore.webstore.network;

import android.os.AsyncTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by LAP10572-local on 7/2/2016.
 */
public class HttpClientHelper {
    public static void executeHttpGetRequest(String url, HttpRequestListener callbackListener){
        new HttpGetRequest().execute(url,callbackListener);
    }
}
