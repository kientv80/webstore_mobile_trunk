package com.webstore.webstore.core;

import com.webstore.webstore.R;
import com.webstore.webstore.network.HttpClientHelper;
import com.webstore.webstore.network.HttpRequestListener;

import java.util.Formatter;

/**
 * Created by LAP10572-local on 8/17/2016.
 */
public class TemplateService {
    public static String getArticleTemplate(){
        /*
        HttpClientHelper.executeHttpGetRequest("http://360hay.com/template/article", new HttpRequestListener() {
            @Override
            public void onRecievedData(Object data) {
                String template = data.toString();
                //store in file and use later.
            }
        });*/
        StringBuilder template = new StringBuilder();
        template.append("<div class='article'>\n" +
                        "\t<h3>%s</h3>\n" +
                        "\t<p  style='color:#999;'>%s <b>%s</b></p>\n" +
                        "\t<p>%s</p>\n" +
                        "\t<img src=%s>\n" +
                        "<p style='background-color:#e9ebee;'>" +
                        "<img src='file:///android_asset/loading.gif' style='padding:8px;'>" +
                        "<span> %s </span>" +
                        "</p>" +
                        "</div>");
        return template.toString();
    }
}
