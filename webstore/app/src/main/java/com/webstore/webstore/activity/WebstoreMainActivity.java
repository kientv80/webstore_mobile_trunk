package com.webstore.webstore.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.webstore.webstore.R;
import com.webstore.webstore.core.LocationService;
import com.webstore.webstore.openapi.CoreOpenAPIs;
import com.webstore.webstore.openapi.View;
import com.webstore.webstore.storage.LocalStorageHelper;

public class WebstoreMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    WebView webView = null;
    ProgressDialog pd = null;
    FloatingActionButton floatingBtn = null;
    TextView title = null;
    Activity mainActivity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalStorageHelper.init(this);
        initMainActivity();//toolbar, navigation bar
        pd = ProgressDialog.show(this, "", getResources().getString(R.string.msg_web_loading), true);
        configWebView();//webview
        registerButtonEvent();//button on action bar
        openUrl("http://360hay.com/news");
        title.setText(R.string.nav_news);

        mainActivity = this;
    }

    private void initMainActivity() {
        setContentView(R.layout.main_activity_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        floatingBtn = (FloatingActionButton) findViewById(R.id.floatingBtn);
        title = (TextView) findViewById(R.id.title);
    }

    private void registerButtonEvent() {
        floatingBtn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if(webView != null && webView.getUrl() != null && webView.getUrl().startsWith("http://360hay.com/news")){
                    openUrl("http://360hay.com/category/news");
                    title.setText(R.string.cate);
                }else{
                    openUrl("http://360hay.com/news");
                    title.setText(R.string.nav_news);
                }
            }
        });
    }
    private void showHideIcons(String url){
        if(url.endsWith("news")) {
            floatingBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_apps_white_24dp));
        }else {
            floatingBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_home_white_24dp));
        }

    }

    private void configWebView() {
        WebView wv = (WebView) findViewById(R.id.webview);
        if(wv != null) {
            wv.addJavascriptInterface(new View(this),"Android");
            wv.addJavascriptInterface(new CoreOpenAPIs(this),"AndroidWebstoreAPIs");
            wv.getSettings().setJavaScriptEnabled(true);
            wv.getSettings().setDatabaseEnabled(true);
            wv.getSettings().setDomStorageEnabled(true);
            wv.getSettings().setAppCacheEnabled(true);
            wv.setScrollBarStyle(android.view.View.SCROLLBARS_INSIDE_OVERLAY);
            wv.setWebChromeClient(new WebChromeClient(){});
            wv.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if(url.startsWith("http://360hay.com")){
                        view.loadUrl(url);
                    }else{
                        Intent intent = new Intent(getApplicationContext(), OpenArticleActivity.class);
                        intent.putExtra("url",url);
                        intent.putExtra("article",false);
                        startActivity(intent);
                    }
                    return true;
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {
                    pd.show();
                    timerDelayRemoveDialog(3000,pd);
                }

                @Override
                public void onPageFinished(WebView view, String url)
                {
                    pd.dismiss();
                    super.onPageFinished(view, url);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    final String mimeType = "text/html";
                    final String encoding = "UTF-8";
                    String html = "<link rel=\"stylesheet\" href=\"file:///android_asset/style.css\">  <b class='red_color'>Không thể mở nội dung</b></br>Vui lòng kiểm tra lại kết nối internet.";
                    view.loadDataWithBaseURL("file:///android_asset/", html, mimeType, encoding, "");
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }

    private void openUrl(String url){
        showHideIcons(url);
        getWebView().loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            getWebView().goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    private WebView getWebView(){
        if(webView == null)
            webView = (WebView)findViewById(R.id.webview);
        return webView;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menuitems, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_news) {
            // Handle the camera action
            openUrl("http://360hay.com/news");
            title.setText(R.string.nav_news);
        }  else if (id == R.id.nav_webapps) {
            openUrl("http://360hay.com/category/webapps");
            title.setText(R.string.nav_webapps);
        } else if (id == R.id.nav_webstore) {
            openUrl("http://360hay.com/webstore");
            title.setText(R.string.nav_webstore);
        } else if (id == R.id.nav_music_film) {
            openUrl("http://360hay.com/cate/nhacphim");
            title.setText(R.string.nav_music_film);
        } else if (id == R.id.nav_foodandhealth) {
            openUrl("http://360hay.com/cate/suckhoe");
            title.setText(R.string.nav_food_health);
        }else if (id == R.id.nav_cooking) {
            openUrl("http://360hay.com/cate/nauan");
            title.setText(R.string.nav_food_cooking);
        }else if (id == R.id.nav_love_family) {
            openUrl("http://360hay.com/cate/tinhyeu");
            title.setText(R.string.nav_love);
        }else if (id == R.id.nav_famous) {
            openUrl("http://360hay.com/cate/ngoisao");
            title.setText(R.string.nav_entertainment);
        }else if (id == R.id.nav_findaround) {
            if(LocationService.isLocationPermissionGrant(getApplicationContext())){
                LocationService.requestLocationPermission(this,getApplicationContext());
            }else{
                Location location = LocationService.getLocation(this,getApplicationContext());
                LocationService.saveLocation(location);
            }
            openUrl("http://360hay.com/location/nearby");
            title.setText(R.string.nav_findaround);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }
    public void timerDelayRemoveDialog(long time, final Dialog d){
        new Handler().postDelayed(new Runnable() {
            public void run() {
                d.dismiss();
            }
        }, time);
    }
    private void animate(final WebView view) {
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        view.startAnimation(anim);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LocationService.MY_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Location location = LocationService.getLocation(this,getApplicationContext());
                    LocationService.saveLocation(location);
                }
            }
        }
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface
                                        dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getApplicationContext().startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
