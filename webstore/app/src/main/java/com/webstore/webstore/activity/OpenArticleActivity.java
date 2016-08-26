package com.webstore.webstore.activity;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import com.webstore.webstore.R;

public class OpenArticleActivity extends AppCompatActivity {
    public static final String MYTYPE = "text/html";
    public static final String UTF_8 = "UTF-8";
    WebView wvDetail = null;
    WebView wvDetailTmp = null;
    boolean haveShortDesc = false;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_article_activity_app_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (wvDetail == null)
            wvDetail = (WebView) findViewById(R.id.webview_detail);
        if (wvDetailTmp == null)
            wvDetailTmp = (WebView) findViewById(R.id.webview_detail_tmp);
        wvDetailTmp.setVisibility(View.INVISIBLE);

        if (getIntent().getBooleanExtra("article", true)) {
            String html = getIntent().getStringExtra("articleHtml");
            if (html != null && !html.isEmpty()) {
                html = "<link rel=\"stylesheet\" href=\"file:///android_asset/style.css\"> "+ html;
                wvDetailTmp.loadDataWithBaseURL("file:///android_asset/style.css", html, MYTYPE, UTF_8, "");
                timerDelayToShowArticle(200);
            }
            String shortDesc = getIntent().getStringExtra("shortDesc");
            if (shortDesc != null && !shortDesc.isEmpty())
                haveShortDesc = true;
        }
        wvDetail.setVisibility(View.INVISIBLE);
        wvDetail.getSettings().setJavaScriptEnabled(true);
        wvDetail.getSettings().setDatabaseEnabled(true);
        wvDetail.getSettings().setDomStorageEnabled(true);
        wvDetail.setWebChromeClient(new WebChromeClient());
        wvDetail.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!haveShortDesc){
                    timerDelayToLoadPage(4000);
                }else{
                    timerDelayToLoadPage(10000);
                }
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            }
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            }
        });
        //Show the detail page after usr touch the wvDetailTmp 3 seconds
        wvDetailTmp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!loading) {
                    loading = true;
                    timerDelayToLoadPage(3000);
                }
                return false;
            }
        });
        String currentUrl = getIntent().getStringExtra("url");
        wvDetail.loadUrl(currentUrl);
    }
    private void animate(final WebView view, int mode) {
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(),mode);
        view.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvDetail.loadUrl("about:blank");
        wvDetail.stopLoading();
        wvDetail.setWebChromeClient(null);
        wvDetail.setWebViewClient(null);
        wvDetail.destroy();
        wvDetail = null;
        wvDetailTmp.destroy();
        wvDetailTmp = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        wvDetail.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wvDetail != null) {
            wvDetail.onPause();
        }
    }

    public void timerDelayToLoadPage(long time) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (wvDetailTmp != null && wvDetailTmp.getVisibility() == View.VISIBLE) {
                    animate(wvDetailTmp, android.R.anim.slide_out_right);
                    wvDetailTmp.setVisibility(View.INVISIBLE);
                }
                if (wvDetail != null && wvDetail.getVisibility() == View.INVISIBLE) {
                    wvDetail.setVisibility(View.VISIBLE);
                    animate(wvDetail, android.R.anim.slide_in_left);
                }
            }
        }, time);
    }
    public void timerDelayToShowArticle(long time) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(wvDetailTmp != null && wvDetailTmp.getVisibility()== View.INVISIBLE) {
                    wvDetailTmp.setVisibility(View.VISIBLE);
                    animate(wvDetailTmp, android.R.anim.slide_in_left);
                }
            }
        }, time);
    }

}
