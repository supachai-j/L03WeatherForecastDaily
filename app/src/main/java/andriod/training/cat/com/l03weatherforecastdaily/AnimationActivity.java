package andriod.training.cat.com.l03weatherforecastdaily;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Supachai.ja on 11/8/2017.
 */


public class AnimationActivity extends AppCompatActivity {

    static final String LOG_TAG = "#####==LOG==#####";

    static final int Connection_Write_Timeout = 15; // sec
    static final int Connection_Read_Timeout = 30; // sec
    static final String API_URL = "http://data.tmd.go.th/api/WeatherForecastDaily/V1/?type=json";
    private ImageView imv_cloud;
    private String response;

    //ImageView imv_cloud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        imv_cloud = (ImageView) findViewById(R.id.lo_imv_cloud);

        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f, 20.0f, 0.0f);
        animation.setDuration(1000);
        animation.setRepeatCount(1);
        animation.setRepeatMode(2); // reverse
        animation.setFillAfter(true);

        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                //imv_cloud.startAnimation(animation); // loop
                if (response != null) {
                    gotoMainPage(response);
                } else {
                    new HttpGet().execute();
                    imv_cloud.startAnimation(animation);
                }

            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        imv_cloud.startAnimation(animation);
        new HttpGet().execute();

    }
    public void gotoMainPage(String response) {
        Intent subIntent = new Intent(getApplicationContext(), MainActivity.class);
        subIntent.putExtra("response", response);
        subIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(subIntent);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
    }
    public void updateData(String response_data) {
        response = response_data;
    }
    private class HttpGet extends AsyncTask<String , Void ,String> {
        protected void onPreExecute() {
            Log.d(LOG_TAG, "onPreExecute");
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, "onPostExecute");
            updateData(result);
        }
        @Override
        protected String doInBackground(String... strings) {
            String http_response = null;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(Connection_Write_Timeout, TimeUnit.SECONDS)
                    .writeTimeout(Connection_Write_Timeout, TimeUnit.SECONDS)
                    .readTimeout(Connection_Read_Timeout, TimeUnit.SECONDS)
                    .build();
            Request request = new Request.Builder()
                    .url(API_URL)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                http_response = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "printStackTrace::"+e.toString());
            }
            Log.d(LOG_TAG, "http_response::"+http_response);
            return http_response;
        }
    }


}