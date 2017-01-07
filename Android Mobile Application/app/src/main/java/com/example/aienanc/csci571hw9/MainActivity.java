package com.example.aienanc.csci571hw9;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Stack;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    public final static String EXTRA_CITY = "com.mycompany.myfirstapp.CITY";
    public final static String EXTRA_STATE = "com.mycompany.myfirstapp.STATE";
    public final static String EXTRA_DEGREE = "com.mycompany.myfirstapp.DEGREE";
    private String response;
    public final static String[] stateVal = {
            "",
            "AL",
            "AK",
            "AZ",
            "AR",
            "CA",
            "CO",
            "CT",
            "DE",
            "DC",
            "FL",
            "GA",
            "HI",
            "ID",
            "IL",
            "IN",
            "IA",
            "KS",
            "KY",
            "LA",
            "ME",
            "MD",
            "MA",
            "MI",
            "MN",
            "MS",
            "MO",
            "MT",
            "NE",
            "NV",
            "NH",
            "NJ",
            "NM",
            "NY",
            "NC",
            "ND",
            "OH",
            "OK",
            "OR",
            "PA",
            "RI",
            "SC",
            "SD",
            "TN",
            "TX",
            "UT",
            "VT",
            "VA",
            "WA",
            "WV",
            "WI",
            "WY"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner stateSpinner = (Spinner) findViewById(R.id.state);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.state_arr, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        stateSpinner.setAdapter(stateAdapter);

        alertTextSet();


//        EditText street = (EditText) findViewById(R.id.street);
//        street.setText("la");
//        EditText city = (EditText) findViewById(R.id.city);
//        city.setText("la");
//        stateSpinner.setSelection(5);
//        search(new View(this));


//        stateSpinner.setOnItemSelectedListener(stateOnItemSelectedListener);
    }
    private void alertTextSet(){
        EditText street = (EditText) findViewById(R.id.street);
        street.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            EditText street = (EditText) findViewById(R.id.street);
            EditText city = (EditText) findViewById(R.id.city);
            Spinner stateSpinner = (Spinner) findViewById(R.id.state);
            TextView alertInfo = (TextView) findViewById(R.id.alertInfo);
            String alertText = "";
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    if(street.getText().toString().equals("")) { // If there is no street address,
                        alertText = "Street Address";
                        alertInfo.setText("Please enter a " + alertText + ".");
                    } else {
                        alertInfo.setText("");
                    }
                } else {
                    if(street.getText().toString().equals("")) { // If there is no street address,
                        alertText = "Street Address";
                        alertInfo.setText("Please enter a " + alertText + ".");
                    } else {    // If there is street address,
                        if(city.getText().toString().equals("")) { // If there is no city,
                            alertText = "City";
                            alertInfo.setText("Please enter a " + alertText + ".");
                        } else {    // If there is city address
                            if(stateSpinner.getSelectedItemPosition() == 0) {    // If there is no state,
                                alertText = "State";
                                alertInfo.setText("Please select a " + alertText + ".");
                            } else {
                                alertInfo.setText("");
                            }
                        }
                    }
                }
            }
        });
        EditText city = (EditText) findViewById(R.id.city);
        city.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            EditText street = (EditText) findViewById(R.id.street);
            EditText city = (EditText) findViewById(R.id.city);
            Spinner stateSpinner = (Spinner) findViewById(R.id.state);
            TextView alertInfo = (TextView) findViewById(R.id.alertInfo);
            String alertText = "";

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (city.getText().toString().equals("")) { // If there is no street address,
                        alertText = "City";
                        alertInfo.setText("Please enter a " + alertText + ".");
                    } else {
                        alertInfo.setText("");
                    }
                } else {
                    if(street.getText().toString().equals("")) { // If there is no street address,
                        alertText = "Street Address";
                        alertInfo.setText("Please enter a " + alertText + ".");
                    } else {    // If there is street address,
                        if(city.getText().toString().equals("")) { // If there is no city,
                            alertText = "City";
                            alertInfo.setText("Please enter a " + alertText + ".");
                        } else {    // If there is city address
                            if(stateSpinner.getSelectedItemPosition() == 0) {    // If there is no state,
                                alertText = "State";
                                alertInfo.setText("Please select a " + alertText + ".");
                            } else {
                                alertInfo.setText("");
                            }
                        }
                    }
                }
            }
        });
        Spinner stateSpinner = (Spinner) findViewById(R.id.state);
//        stateSpinner.setFocusable(true);
//        stateSpinner.setFocusableInTouchMode(true);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            EditText street = (EditText) findViewById(R.id.street);
            EditText city = (EditText) findViewById(R.id.city);
            Spinner stateSpinner = (Spinner) findViewById(R.id.state);
            TextView alertInfo = (TextView) findViewById(R.id.alertInfo);
            String alertText = "";

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(stateSpinner.getSelectedItemPosition() == 0) {    // If there is no state,
                    alertText = "State";
                    alertInfo.setText("Please select a " + alertText + ".");
                } else {
                    if(street.getText().toString().equals("")) { // If there is no street address,
                        alertText = "Street Address";
                        alertInfo.setText("Please enter a " + alertText + ".");
                    } else {    // If there is street address,
                        if(city.getText().toString().equals("")) { // If there is no city,
                            alertText = "City";
                            alertInfo.setText("Please enter a " + alertText + ".");
                        } else {    // If there is city address
                            if(stateSpinner.getSelectedItemPosition() == 0) {    // If there is no state,
                                alertText = "State";
                                alertInfo.setText("Please select a " + alertText + ".");
                            } else {
                                alertInfo.setText("");
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

//    AdapterView.OnItemSelectedListener stateOnItemSelectedListener =
//            new AdapterView.OnItemSelectedListener(){
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    String statePos = (String)parent.getItemAtPosition(position);
//                    TextView alertInfo = (TextView) findViewById(R.id.alertInfo);
//                    alertInfo.setText(statePos);
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {}
//            };

    public void search(View view) { // When SEARCH button is clicked,
        TextView alertInfo = (TextView) findViewById(R.id.alertInfo);
        boolean error = false;
        String alertText = "";
        EditText street = (EditText) findViewById(R.id.street);
        if(street.getText().toString().equals("")){ // If there is no street address,
            alertText += "Street Address";
            error = true;
        }
        EditText city = (EditText) findViewById(R.id.city);
        if(city.getText().toString().equals("")){   // If there is no city,
            if(error) {
                alertText += ", City";
            } else {
                alertText += "City";
            }
            error = true;
        }
        Spinner stateSpinner = (Spinner) findViewById(R.id.state);
        if(stateSpinner.getSelectedItemPosition() == 0){    // If there is no state,
            if(error) {
                alertText += ", State";
            } else {
                alertText += "State";
            }
            error = true;
        }
        if(error){  // If there was empty field,
            alertInfo.setText("Please enter " + alertText + ".");
        } else {    // If all values are inserted,
            alertInfo.setText("");
            requestToServer();
        }
    }

    private void requestToServer(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        Uri.Builder builder = new Uri.Builder();    // Make URI
        String streetaddress = (((EditText) findViewById(R.id.street)).getText()).toString();
        String city = (((EditText)findViewById(R.id.city)).getText()).toString();
        String state = this.stateVal[((Spinner)findViewById(R.id.state)).getSelectedItemPosition()];
        String degree = ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.degree)).getCheckedRadioButtonId())).getText().toString().toLowerCase();
        builder.scheme("http")
                .authority("ihur-env.elasticbeanstalk.com")
                .appendPath("server.php")
                .appendQueryParameter("streetaddress", streetaddress)
                .appendQueryParameter("city", city)
                .appendQueryParameter("state", state)
                .appendQueryParameter("degree", degree);
        String url = builder.build().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String response){
                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putExtra(EXTRA_MESSAGE, response);
                intent.putExtra(EXTRA_CITY, (((EditText)findViewById(R.id.city)).getText()).toString());
                intent.putExtra(EXTRA_STATE, MainActivity.stateVal[((Spinner)findViewById(R.id.state)).getSelectedItemPosition()]);
                intent.putExtra(EXTRA_DEGREE, ((RadioButton) findViewById(((RadioGroup) findViewById(R.id.degree)).getCheckedRadioButtonId())).getText().toString().toLowerCase());
                startActivity(intent);
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                TextView alertInfo = (TextView) findViewById(R.id.alertInfo);
                alertInfo.setText("onErrorResponse");
            }
        });
        queue.add(stringRequest);
    }

    public void clear(View view) { // When CLEAR button is clicked,
        EditText street = (EditText) findViewById(R.id.street);
        street.setText("");
        EditText city = (EditText) findViewById(R.id.city);
        city.setText("");
        Spinner stateSpinner = (Spinner) findViewById(R.id.state);
        stateSpinner.setSelection(0);
        RadioButton fahrenheit = (RadioButton)findViewById(R.id.fahrenheit);
        fahrenheit.setChecked(true);
    }

    public void about(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
//        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
//        alertDialog.setTitle("Alert");
//        alertDialog.setMessage("about");
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
    }

    public void forecast(View view){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forecast.io"));
        startActivity(browserIntent);
    }

}
