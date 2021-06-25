package com.example.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView resultTextView;
    TextView mainTextView;
    EditText cityEditText;
    ImageView imageView;

    private final String url = "http://api.openweathermap.org/data/2.5/weather";
    private final String appId = "27f6d993120afbe80a95cf84524a0170";
    DecimalFormat df = new DecimalFormat("#.##");


    public void getWeather(View view){
        String tempUrl = "";
        String city = cityEditText.getText().toString().trim();
        if(city.equals("")){
            resultTextView.setText("City field cannot be empty!");
        } else{
            tempUrl = url + "?q=" + city + "&appid=" + appId;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
//                    Log.d("Response : ", response);
                    String output = "";
                    try {
                        //Main JSON Object
                        JSONObject jsonResponse = new JSONObject(response);

                        //Main JSON ARRAY
                        JSONArray jsonArray = jsonResponse.getJSONArray("weather");

                        //JSON Components - 1
                        JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                        String description = jsonObjectWeather.getString("description");

                        //JSON Components - 2
                        JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                        double temp = jsonObjectMain.getDouble("temp") - 273.15;
                        double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                        float pressure = jsonObjectMain.getInt("pressure");
                        int humidity = jsonObjectMain.getInt("humidity");

                        //JSON Components - 3
                        JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                        String wind = jsonObjectWind.getString("speed");

                        //JSON Components - 4
                        JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                        String clouds = jsonObjectClouds.getString("all");

                        //JSON Components - 5
                        JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                        String countryName = jsonObjectSys.getString("country");
                        String cityName = jsonResponse.getString("name");

                        output += "Current weather of " + cityName + " (" + countryName + ")"
                                + "\n Temp: " + df.format(temp) + " °C"
                                + "\n Feels Like: " + df.format(feelsLike) + " °C"
                                + "\n Humidity: " + humidity + "%"
                                + "\n Description: " + description
                                + "\n Wind Speed: " + wind + " m/s (meters per second)"
                                + "\n Cloudiness: " + clouds + "%"
                                + "\n Pressure: " + pressure + " hPa";
                        resultTextView.setText(output);
                        if(temp < 10.0){
                            imageView.setImageResource(R.drawable.winterr);
                            mainTextView.setTextColor(Color.WHITE);
                            cityEditText.setHintTextColor(Color.WHITE);
                            cityEditText.setTextColor(Color.WHITE);
                        } else if(temp < 20.0){
                            imageView.setImageResource(R.drawable.rainyy);
                            mainTextView.setTextColor(Color.WHITE);
                            cityEditText.setHintTextColor(Color.WHITE);
                            cityEditText.setTextColor(Color.WHITE);
                        } else if(temp < 30){
                            imageView.setImageResource(R.drawable.sunnyyy);
                            cityEditText.setHintTextColor(Color.BLACK);
                            cityEditText.setTextColor(Color.BLACK);
                            mainTextView.setTextColor(Color.rgb(47,55,51));
                        } else {
                            imageView.setImageResource(R.drawable.hottt);
                            cityEditText.setHintTextColor(Color.BLACK);
                            cityEditText.setTextColor(Color.BLACK);
                            mainTextView.setTextColor(Color.WHITE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Error : Check your internet connection or City Name", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Error : Check your internet connection or City Name", Toast.LENGTH_SHORT).show();
                }
            });

            //To make keyboard GO-Down after button click
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTextView = findViewById(R.id.resultTextView);
        cityEditText = findViewById(R.id.cityEditText);
        imageView = findViewById(R.id.imageView);
        mainTextView = findViewById(R.id.textView);

        mainTextView.setTextColor(Color.WHITE);
        cityEditText.setHintTextColor(Color.WHITE);
        cityEditText.setTextColor(Color.WHITE);


    }
}