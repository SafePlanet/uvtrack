package com.spi.safetrack;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.spi.track.safetrack.MyFirebaseInstanceIDService;
import com.spi.track.safetrack.R;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements Response.Listener,
        Response.ErrorListener {

    public static final String REQUEST_TAG = "MainActivity";
    public static String globalValue;
    private RequestQueue mQueue;
    String fcmtoken;
    String url;
    private static MainActivity singleton;

    public static MainActivity getInstance() {
        return singleton;
    }
    WebView myWebView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        singleton = this;


     //   WebView myWebView = (WebView) findViewById(R.id.webView1);

       // myWebView.setWebViewClient(new SPIWebViewClient(this));
        final WebView myWebView = (WebView) findViewById(R.id.webView1);
         myWebView.getSettings().setJavaScriptEnabled(true);
         myWebView.getSettings().setLoadWithOverviewMode(true);
         //myWebView.getSettings().setUseWideViewPort(true);

        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( URLUtil.isNetworkUrl(url) ) {
                    return false;
                }

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity( intent );
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                WebView wv= (WebView)view;

              if (url.equals("http://www.trackho.com/safeplanet/welcome.html#/")) {
                    wv.evaluateJavascript("function guim(){return window.userId ||'na';}guim();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(final String s) {
                            globalValue = s;
                            String UserId= s.replace("\"", "");
                            MyFirebaseInstanceIDService myfcm = new MyFirebaseInstanceIDService();
                            fcmtoken = myfcm.getFcm();
                            Log.e("fcm id", fcmtoken);
                            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                            String url = "http://www.trackho.com/safeplanet/user/saveFCMToken/"+UserId+"/"+fcmtoken;
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            // Display the response string.
                                           // _response.setText(response);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                   // _response.setText("That didn't work!");
                                }
                            })
                            {
                                //adding parameters to the request
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("s", globalValue.toString());
                                    params.put("fcmtoken", fcmtoken.toString());
                                    return params;
                                }
                            };
                            queue.add(stringRequest);

                        }
                    });

                }

          }
        });
        myWebView.loadUrl("http://www.trackho.com/safeplanet/index.html");
    }


    @Override
    public void onBackPressed() {
        WebView myWebView = (WebView) findViewById(R.id.webView1);
        //final WebView myWebView = (WebView) findViewById(R.id.webView1);
        if(myWebView!=null)
        {
            if(myWebView.canGoBack())
            {
                myWebView.goBack();
            }
            else
            {
               // super.onBackPressed();
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        }
    }


    @Override
    public void onErrorResponse(VolleyError error) {

    }
    @Override
    public void onResponse(Object response) {

    }
}
