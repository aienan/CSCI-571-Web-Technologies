package com.example.aienanc.csci571hw9;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DetailsActivity extends AppCompatActivity {
    private final int totalHour = 24;
    private final int totalDays = 7;
    private enum weekday {Sunday, Monday, Tuesday, Wednesday, Thursday, Friday, Saturday};
    private final String[] bgColor = {"#FFDB6A", "#A0E7FF", "#FFC4EA", "#C4FFA5", "#FFBDB7", "#EFFFB5", "#BCBEFF"};
    private final String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private String response;
    private String city;
    private String state;
    private String degree;
    private String tempDeg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("DetailsActivity");
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.response = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        this.city = intent.getStringExtra(MainActivity.EXTRA_CITY);
        this.state = intent.getStringExtra(MainActivity.EXTRA_STATE);
        this.degree = intent.getStringExtra(MainActivity.EXTRA_DEGREE);
        if(this.degree.equals("fahrenheit")){
            this.tempDeg = "\u2109";
        } else {
            this.tempDeg = "\u2103";
        }

        TextView detailsTitle = (TextView) findViewById(R.id.detailsTitle);
        detailsTitle.setText("More Details for " + city + ", " + state);


        next24Hours(1);
        next7Days();
        next24HoursView(new View(this));

    }

    private void next24Hours(int start){
        try{
            JSONObject obj = new JSONObject(this.response);  // JSON Object
            TextView tempTitle = (TextView) findViewById(R.id.tempTitle);
            tempTitle.setText("Temp(" + this.tempDeg + ")");
            LinearLayout next24Hours = (LinearLayout)findViewById(R.id.next24Hours);
            LinearLayout next24HoursItem = (LinearLayout)findViewById(R.id.next24HoursItem);
            String timeZoneStr = obj.getString("timezone");
            int timeZoneOffset = obj.getInt("offset");
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            sdf.setTimeZone(tz);
            Calendar cal = new GregorianCalendar();
            cal.setTimeZone(tz);

            for(int i=start; i < this.totalHour + start; i++) {
                LinearLayout LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.HORIZONTAL);
                if(i % 2 == 1) {
                    LL.setBackgroundResource(R.color.gray);
                }
                LL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                int pad = pixelToDp(5);
                LL.setPadding(pad, pad, pad, pad);
                // JSON Object data
                JSONObject data = obj.getJSONObject("hourly").getJSONArray("data").getJSONObject(i);

                // nextTime
                LinearLayout L1 = new LinearLayout(this);
                LinearLayout.LayoutParams L1LP = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
                long nextTime = data.getLong("time");
                cal.setTimeInMillis(nextTime * 1000);
                TextView nextTimeView = new TextView(this);
                nextTimeView.setTextSize(16);
                nextTimeView.setTypeface(nextTimeView.getTypeface(), Typeface.BOLD);
                nextTimeView.setText(sdf.format(cal.getTime()));
                L1.addView(nextTimeView);

                // summary Img
                LinearLayout L2 = new LinearLayout(this);
                LinearLayout.LayoutParams L2LP = new LinearLayout.LayoutParams(0, pixelToDp(30), 0.4f);
                L2.setGravity(Gravity.CENTER);
                L2LP.gravity = Gravity.CENTER_HORIZONTAL;
                String summary = data.getString("icon");
                ImageView summaryViewObj = new ImageView(this);
                ImageView summaryView = iconFunc(summaryViewObj, summary);
                L2.addView(summaryView);

                // temperature
                LinearLayout L3 = new LinearLayout(this);
                LinearLayout.LayoutParams L3LP = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.3f);
                L3.setPadding(0, 0, pixelToDp(10), 0);
                L3.setGravity(Gravity.RIGHT);
                double temperature = data.getDouble("temperature");
                TextView temperatureView = new TextView(this);
                temperatureView.setTextSize(16);
//                temperatureView.setGravity(Gravity.RIGHT);
                temperatureView.setText(Long.toString(Math.round(temperature)));
                L3.addView(temperatureView);

                // Add all components
                LL.addView(L1, L1LP);
                LL.addView(L2, L2LP);
                LL.addView(L3, L3LP);
                next24HoursItem.addView(LL);
            }
            LinearLayout blueBox = (LinearLayout)findViewById(R.id.blueBox);
            if((this.totalHour + start)%2 == 1){
                blueBox.setBackgroundResource(R.color.gray);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private int pixelToDp(int pixel){
        float density = this.getResources().getDisplayMetrics().density;
        return (int)(pixel * density);
    }

    private ImageView iconFunc(ImageView summaryView, String summary){  // Set icon img

        if(summary.equals("clear-day")){
            summaryView.setImageResource(R.drawable.clear);
        } else if (summary.equals("clear-night")){
            summaryView.setImageResource(R.drawable.clear_night);
        } else if (summary.equals("rain")){
            summaryView.setImageResource(R.drawable.rain);
        } else if (summary.equals("snow")){
            summaryView.setImageResource(R.drawable.snow);
        } else if (summary.equals("sleet")){
            summaryView.setImageResource(R.drawable.sleet);
        } else if (summary.equals("wind")){
            summaryView.setImageResource(R.drawable.wind);
        } else if (summary.equals("fog")){
            summaryView.setImageResource(R.drawable.fog);
        } else if (summary.equals("cloudy")){
            summaryView.setImageResource(R.drawable.cloudy);
        } else if (summary.equals("partly-cloudy-day")){
            summaryView.setImageResource(R.drawable.cloud_day);
        } else if (summary.equals("partly-cloudy-night")){
            summaryView.setImageResource(R.drawable.cloud_night);
        } else {
            summaryView.setImageResource(R.drawable.cloudy);
        }
        return summaryView;
    }

    public void next24HoursView(View view) {
        Button next24HoursButton = (Button) findViewById(R.id.next24HoursButton);
        next24HoursButton.setBackgroundResource(R.color.blueBox);
        Button next7DaysButton = (Button) findViewById(R.id.next7DaysButton);
        next7DaysButton.setBackgroundResource(R.color.grayButton);
        LinearLayout next24Hours = (LinearLayout) findViewById(R.id.next24Hours);
        next24Hours.setVisibility(View.VISIBLE);
        LinearLayout next7Days = (LinearLayout) findViewById(R.id.next7Days);
        next7Days.setVisibility(View.GONE);

    }

    private void next7Days(){
        try{
            JSONObject obj = new JSONObject(this.response);  // JSON Object
            LinearLayout next7Days = (LinearLayout)findViewById(R.id.next7Days);
            String timeZoneStr = obj.getString("timezone");
            int timeZoneOffset = obj.getInt("offset");
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMM dd");
            TimeZone tz = TimeZone.getTimeZone(timeZoneStr);
            sdf.setTimeZone(tz);

            for(int i=1; i <= this.totalDays; i++) {
                JSONObject data = obj.getJSONObject("daily").getJSONArray("data").getJSONObject(i);
                LinearLayout LL = new LinearLayout(this);
                LL.setOrientation(LinearLayout.VERTICAL);
                LL.setBackgroundColor(Color.parseColor(this.bgColor[i - 1]));
                int dp5 = pixelToDp(5);
                LL.setPadding(dp5, dp5, dp5, dp5);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, dp5, 0, dp5);

                LinearLayout L1 = new LinearLayout(this);
                L1.setOrientation(LinearLayout.HORIZONTAL);
                TextView dateView = new TextView(this);
                dateView.setTextSize(16);
                dateView.setTypeface(dateView.getTypeface(), Typeface.BOLD);
                Calendar cal = new GregorianCalendar();
                cal.setTimeZone(tz);
                cal.setTimeInMillis(data.getLong("time") * 1000);
                dateView.setText(sdf.format(cal.getTime()));

                LinearLayout L2 = new LinearLayout(this);
                LinearLayout.LayoutParams L2LP = new LinearLayout.LayoutParams(0, pixelToDp(30), 0.2f);
                L2.setGravity(Gravity.RIGHT);
                L2LP.gravity = Gravity.CENTER_HORIZONTAL;
                String summary = data.getString("icon");
                ImageView summaryViewObj = new ImageView(this);
                ImageView summaryView = iconFunc(summaryViewObj, summary);
                L2.addView(summaryView);

                TextView temperatureView = new TextView(this);
                temperatureView.setTextSize(16);
                temperatureView.setText("Min: " + Math.round(data.getLong("temperatureMin")) + this.tempDeg + " | Max: " + Math.round(data.getLong("temperatureMax")) + this.tempDeg);


                // Add all components
                LinearLayout.LayoutParams L1LP = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f);
                L1.addView(dateView, L1LP);
                L1.addView(L2, L2LP);
                LL.addView(L1);
                LL.addView(temperatureView);
                next7Days.addView(LL, layoutParams);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void next7DaysView(View view) {
        Button next7DaysButton = (Button) findViewById(R.id.next7DaysButton);
        next7DaysButton.setBackgroundResource(R.color.blueBox);
        Button next24HoursButton = (Button) findViewById(R.id.next24HoursButton);
        next24HoursButton.setBackgroundResource(R.color.grayButton);
        LinearLayout next7Days = (LinearLayout) findViewById(R.id.next7Days);
        next7Days.setVisibility(View.VISIBLE);
        LinearLayout next24Hours = (LinearLayout) findViewById(R.id.next24Hours);
        next24Hours.setVisibility(View.GONE);

    }

    public void blueBox(View view) {
        LinearLayout blueBox = (LinearLayout) findViewById(R.id.blueBox);
        blueBox.setVisibility(View.GONE);
        next24Hours(this.totalHour + 1);
    }
}
