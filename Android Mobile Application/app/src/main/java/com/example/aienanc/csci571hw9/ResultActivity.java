package com.example.aienanc.csci571hw9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.hamweather.aeris.communication.AerisEngine;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ResultActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public final static String EXTRA_CITY = "com.mycompany.myfirstapp.CITY";
    public final static String EXTRA_STATE = "com.mycompany.myfirstapp.STATE";
    public final static String EXTRA_DEGREE = "com.mycompany.myfirstapp.DEGREE";
    public final static String EXTRA_LAT = "com.mycompany.myfirstapp.EXTRA_LAT";
    public final static String EXTRA_LON = "com.mycompany.myfirstapp.EXTRA_LON";
    private String response;
    private String city;
    private String state;
    private String degree;
    private String tempDeg;
    private String speedUnit;
    private String lengthUnit;
    private double lat;
    private double lon;
    private Uri todayImg;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private String summary;
    private long temperature;
    private boolean inFacebook = false;
    private boolean isKeyBack = false;
    final int FB_CALLBACK = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("ResultActivity");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.response = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        this.city = intent.getStringExtra(MainActivity.EXTRA_CITY);
        this.state = intent.getStringExtra(MainActivity.EXTRA_STATE);
        this.degree = intent.getStringExtra(MainActivity.EXTRA_DEGREE);
        if(this.degree.equals("fahrenheit")){
            this.tempDeg = "\u2109";
            this.speedUnit = "mph";
            this.lengthUnit = "mi";
        } else {
            this.tempDeg = "\u2103";
            this.speedUnit = "m/s";
            this.lengthUnit = "km";
        }
        parseResponse(this.response);


    }
    private void parseResponse(String response){
        try{
            JSONObject obj = new JSONObject(response);  // JSON Object
            this.lat = obj.getDouble("latitude");
            this.lon = obj.getDouble("longitude");
            JSONObject icon = obj.getJSONObject("currently");   // Set icon img
            String iconVar = icon.getString("icon");
            iconFunc(iconVar);
            this.summary = obj.getJSONObject("currently").getString("summary");   // Set summary text
            TextView summaryText = (TextView) findViewById(R.id.summary);
            summaryText.setText(summary + " in " + city + ", " + state);
            this.temperature = Math.round(obj.getJSONObject("currently").getDouble("temperature"));
            TextView temperatureText = (TextView) findViewById(R.id.temperature);
            temperatureText.setText(String.valueOf(temperature));
            TextView temperatureDegText = (TextView) findViewById(R.id.temperatureDeg);
            temperatureDegText.setText(this.tempDeg);
            long temperatureLow = Math.round(obj.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMin"));
            long temperatureHigh = Math.round(obj.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getDouble("temperatureMax"));
            TextView temperatureLHText = (TextView) findViewById(R.id.temperatureLH);
            temperatureLHText.setText("L:" + temperatureLow + (char)0x00B0 + " | H:" + temperatureHigh + (char)0x00B0);
            Double precipIntensity = obj.getJSONObject("currently").getDouble("precipIntensity");
            String precipDisplay;
            if(0 <= precipIntensity && precipIntensity < 0.002){
                precipDisplay = "None";
            } else if(0.002 <= precipIntensity && precipIntensity < 0.017){
                precipDisplay = "Very Light";
            } else if(0.017 <= precipIntensity && precipIntensity < 0.1){
                precipDisplay = "Light";
            } else if(0.1 <= precipIntensity && precipIntensity < 0.4){
                precipDisplay = "Moderate";
            } else if(0.4 <= precipIntensity){
                precipDisplay = "Heavy";
            } else {
                precipDisplay = "";
            }
            TextView precipIntensityText = (TextView) findViewById(R.id.precipIntensity);
            precipIntensityText.setText(precipDisplay);
            String chanceOfRain = Math.round(precipIntensity * 100) + "%";
            TextView chanceOfRainText = (TextView) findViewById(R.id.chanceOfRain);
            chanceOfRainText.setText(chanceOfRain);
            String windSpeed = obj.getJSONObject("currently").getDouble("windSpeed") + this.speedUnit;
            TextView windSpeedText = (TextView) findViewById(R.id.windSpeed);
            windSpeedText.setText(windSpeed);
            String dewPoint = Math.round(obj.getJSONObject("currently").getDouble("dewPoint")) + this.tempDeg;
            TextView dewPointText = (TextView) findViewById(R.id.dewPoint);
            dewPointText.setText(dewPoint);
            String humidity = Math.round(obj.getJSONObject("currently").getDouble("humidity") * 100) + "%";
            TextView humidityText = (TextView) findViewById(R.id.humidity);
            humidityText.setText(humidity);
            String visibility = obj.getJSONObject("currently").getDouble("visibility") + this.lengthUnit;
            TextView visibilityText = (TextView) findViewById(R.id.visibility);
            visibilityText.setText(visibility);
            String timeZoneStr = obj.getString("timezone");
            int timeZoneOffset = obj.getInt("offset");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            sdf.setTimeZone(tz);
            Calendar cal = new GregorianCalendar();
            cal.setTimeZone(tz);
            long sunrise = obj.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getLong("sunriseTime");
            cal.setTimeInMillis(sunrise*1000);
            TextView sunriseText = (TextView) findViewById(R.id.sunrise);
            sunriseText.setText(sdf.format(cal.getTime()));
            long sunset = obj.getJSONObject("daily").getJSONArray("data").getJSONObject(0).getLong("sunsetTime");
            cal.setTimeInMillis(sunset*1000);
            TextView sunsetText = (TextView) findViewById(R.id.sunset);
            sunsetText.setText(sdf.format(cal.getTime()));
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
    private void iconFunc(String iconVar){  // Set icon img
        String iconImg;
        ImageView iconImgView = (ImageView) findViewById(R.id.iconImg);
        String imageDomain = "http://ihur-env.elasticbeanstalk.com/image/";
        if(iconVar.equals("clear-day")){
            iconImg = "clear";
            iconImgView.setImageResource(R.drawable.clear);
        } else if (iconVar.equals("clear-night")){
            iconImg = "clear_night";
            iconImgView.setImageResource(R.drawable.clear_night);
        } else if (iconVar.equals("rain")){
            iconImg = "rain";
            iconImgView.setImageResource(R.drawable.rain);
        } else if (iconVar.equals("snow")){
            iconImg = "snow";
            iconImgView.setImageResource(R.drawable.snow);
        } else if (iconVar.equals("sleet")){
            iconImg = "sleet";
            iconImgView.setImageResource(R.drawable.sleet);
        } else if (iconVar.equals("wind")){
            iconImg = "wind";
            iconImgView.setImageResource(R.drawable.wind);
        } else if (iconVar.equals("fog")){
            iconImg = "fog";
            iconImgView.setImageResource(R.drawable.fog);
        } else if (iconVar.equals("cloudy")){
            iconImg = "cloudy";
            iconImgView.setImageResource(R.drawable.cloudy);
        } else if (iconVar.equals("partly-cloudy-day")){
            iconImg = "cloud_day";
            iconImgView.setImageResource(R.drawable.cloud_day);
        } else if (iconVar.equals("partly-cloudy-night")){
            iconImg = "cloud_night";
            iconImgView.setImageResource(R.drawable.cloud_night);
        } else {
            iconImg = "cloudy";
            iconImgView.setImageResource(R.drawable.cloudy);
        }
        this.todayImg = Uri.parse(imageDomain + iconImg + ".png");
    }
    public void moreDetail(View view){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.response);
        intent.putExtra(EXTRA_CITY, this.city);
        intent.putExtra(EXTRA_STATE, this.state);
        intent.putExtra(EXTRA_DEGREE, this.degree);
        startActivity(intent);
    }
    public void viewMap(View view){
        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), this);

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_LAT, this.lat);
        intent.putExtra(EXTRA_LON, this.lon);
        startActivity(intent);
    }
    public void facebook(View view){
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
//        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
//            @Override
//            public void onSuccess(Sharer.Result result) {
//                Context context = getApplicationContext();
//                CharSequence text;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast;
//                text = result.getPostId();
//                toast = Toast.makeText(context, text, duration);
//                toast.show();
//            }
//            @Override
//            public void onCancel() {
//                Context context = getApplicationContext();
//                CharSequence text;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast;
//                text = "Posted Cancelled";
//                toast = Toast.makeText(context, text, duration);
//                toast.show();
//            }
//            @Override
//            public void onError(FacebookException e) {
//                Context context = getApplicationContext();
//                CharSequence text;
//                int duration = Toast.LENGTH_SHORT;
//                Toast toast;
//                text = "Posted Error";
//                toast = Toast.makeText(context, text, duration);
//                toast.show();
//            }
//        }, FB_CALLBACK);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
//            this.inFacebook = true;
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Current Weather in " + this.city + ", " + this.state)
                    .setContentDescription(this.summary + ", " + this.temperature + this.tempDeg)
                    .setContentUrl(Uri.parse("http://forecast.io"))
                    .setImageUrl(this.todayImg)
                    .build();
            shareDialog.show(linkContent);
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)  {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            Context context = getApplicationContext();
//            CharSequence text;
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast;
//            text = String.valueOf(event.getRepeatCount());
//            toast = Toast.makeText(context, text, duration);
//            toast.show();
//
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }

//    @Override
//    public void onResume() {
//        super.onResume();  // Always call the superclass method first
//        // Get the Camera instance as the activity achieves full user focus
//        if (inFacebook == true && isKeyBack == true) {
//            Context context = getApplicationContext();
//            CharSequence text;
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast;
//            text = "Posted Cancelled";
//            toast = Toast.makeText(context, text, duration);
//            toast.show();
//        }
//        inFacebook = false;
//        isKeyBack = false;
//    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        Context context = getApplicationContext();
//        String text;
//        int duration = Toast.LENGTH_LONG;
//        Toast toast;
//        if(resultCode == RESULT_CANCELED) {
//            text = "Posted Cancelled";
//            toast = Toast.makeText(context, text, duration);
//            toast.show();
//        } else if(resultCode == RESULT_OK){
//
//            TextView debug = (TextView) findViewById(R.id.debug);
//            debug.setText(text);
//        }
    }
}
