package com.spi.safetrack;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.spi.track.safetrack.MyFirebaseInstanceIDService;

import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Looper.getMainLooper;

/**
 * Created by Niteesh Mishra on 03-09-2017.
 */

public class SPIWebViewClient extends WebViewClient {
    private final Context ctx;
    Boolean wasSavedFcm = false;
    String fcmtoken;
    public SPIWebViewClient(Context context) {
        ctx = context;

        SharedPreferences prefs = ctx.getSharedPreferences("fcm", ctx.MODE_PRIVATE);
        wasSavedFcm = prefs.getBoolean("wasSaved", false);
    }

    @Override
    public void onPageFinished(final WebView view, String url) {


        super.onPageFinished(view, url);
        if (wasSavedFcm) return;

        if (url.equals("http://www.trackho.com/safeplanet/welcome.html#/")) {
            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.evaluateJavascript("function guim(){return window.userId ||'na';}guim();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(final String s) {
                            MyFirebaseInstanceIDService myfcm = new MyFirebaseInstanceIDService();
                           fcmtoken = myfcm.getFcm();
                            Log.e("fcm id", fcmtoken);
                            try {

                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       URL uri = null;
                                       try {
                                           uri = new URL("http://www.trackho.com/safeplanet/user/SaveFCMToken/" + s + "/" + fcmtoken);

                                           HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
                                           //set the request method to GET
                                           connection.setRequestMethod("GET");
                                           connection.connect();

                                           SharedPreferences.Editor editor = ctx.getSharedPreferences("fcm", ctx.MODE_PRIVATE).edit();
                                           editor.putBoolean("wasSaved", true);
                                           editor.apply();
                                           wasSavedFcm= true;

                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }
                                   }
                               }).start();

                            } catch (Exception e) {
                                Log.e("HTTP GET:", e.toString());
                                SharedPreferences.Editor editor = ctx.getSharedPreferences("fcm", ctx.MODE_PRIVATE).edit();
                                editor.putBoolean("wasSaved", false);
                                editor.apply();
                            }
                        }
                    });
                }
            }, 1000);
        }
    }
}
