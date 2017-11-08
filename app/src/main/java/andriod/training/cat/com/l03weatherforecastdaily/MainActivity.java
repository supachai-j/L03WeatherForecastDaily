package andriod.training.cat.com.l03weatherforecastdaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    static final String LOG_TAG = "#####==LOG==#####";
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv_weather_info = (TextView) findViewById(R.id.lo_tv_weather_info);
        Bundle bundle = getIntent().getExtras();

        if(bundle.getString("response")!= null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                tv_weather_info.setText(Html.fromHtml(showData(bundle.getString("response")), Html.FROM_HTML_MODE_LEGACY));
            }else{
                tv_weather_info.setText(Html.fromHtml(showData(bundle.getString("response"))));
            }
        }else{
            tv_weather_info.setText(getString(R.string.strXML_error_cannot_connect));
        }


    }

    public String showData(String response_data) {
        String result = getString(R.string.strXML_error_cannot_connect);
        if (response_data != null) {
            try {
                JSONObject response_data_json = new JSONObject(response_data);
                if (!response_data_json.isNull("DailyForecast")) {
                    String daily_forecast_json = response_data_json.getString("DailyForecast");
                    JSONObject regions_forecast_json_obj;
                    try {
                        regions_forecast_json_obj = new JSONObject(daily_forecast_json);
                        JSONArray regions_forecast_json_ary = new JSONArray(regions_forecast_json_obj.getString("RegionsForecast"));
                        StringBuilder s = new StringBuilder();
                        for (int i = 0; i < regions_forecast_json_ary.length(); i++) {
                            try {
                                JSONObject row = regions_forecast_json_ary.getJSONObject(i);
                                Log.d(LOG_TAG, row.getString("RegionName"));
                                s.append("<h2><font color=\"#0000ff\">");
                                s.append(row.getString("RegionName"));
                                s.append("</font></h2><p>");
                                s.append(row.getString("Description"));
                                s.append("</p>");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(LOG_TAG, "printStackTrace::" + e.toString());
                            }
                        }
                        result = s.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(LOG_TAG, "printStackTrace::" + e.toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(LOG_TAG, "printStackTrace::"+e.toString());
            }
        }
        return result;
    }



}
