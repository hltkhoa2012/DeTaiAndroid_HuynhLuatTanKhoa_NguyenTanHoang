package com.example.detai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

EditText edtSearch;
TextView txtName,txtCountry,txtCloud,txtTemp,txtHumidity,txtDay,txtWind,txtStatus;
Button btnSearch,btnChange;
ImageView Imgicon;
String City="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhxa();
        GetCurrentWeatherData("Hanoi");
    }
    public void anhxa(){
        Imgicon=(ImageView)findViewById(R.id.ImageIcon);
        edtSearch=(EditText)findViewById(R.id.editextSearch);
        btnSearch=(Button)findViewById(R.id.buttonSearch);
        btnChange=(Button)findViewById(R.id.btnChange);
        txtName=(TextView)findViewById(R.id.textviewName);
        txtCountry=(TextView)findViewById(R.id.textviewCountry);
        txtCloud=(TextView)findViewById(R.id.TextviewMay);
        txtDay=(TextView)findViewById(R.id.TextviewDay);
        txtHumidity=(TextView)findViewById(R.id.TextviewHumidity);
        txtStatus=(TextView)findViewById(R.id.textviewStatus);
        txtTemp=(TextView)findViewById(R.id.textviewTemp);
        txtWind=(TextView)findViewById(R.id.TextviewWind);



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = edtSearch.getText().toString();
                search.replace(" ","+");
                String city = search;
                    if (city.equals("")){
                    City="Hanoi";
                    GetCurrentWeatherData(City);
                }else {
                    City=city;
                    GetCurrentWeatherData(City);
                }

            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                String city = edtSearch.getText().toString();
                intent.putExtra("name",city);
                startActivity(intent);
            }
        });
    }
        public void GetCurrentWeatherData(String data){
            RequestQueue requestQueue = new Volley().newRequestQueue(MainActivity.this);
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=a9baa7bc01541882d0e1aea14e67012d";
            StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String day = jsonObject.getString("dt");
                                String name = jsonObject.getString("name");

                                txtName.setText("Ten Thanh Pho: "+name);

                                long l = Long.valueOf(day);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy HH-mm-ss");
                                String Day = simpleDateFormat.format(date);

                                txtDay.setText(Day);

                                JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                                JSONObject jsonObject1Weather = jsonArrayWeather.getJSONObject(0);
                                String status = jsonObject1Weather.getString("main");
                                String icon = jsonObject1Weather.getString("icon");

                                String iconHinh = "http://openweathermap.org/img/wn/"+icon+".png";
                                Picasso.with(MainActivity.this).load(iconHinh).into(Imgicon);
                                Log.d("iconHinh",iconHinh);
                                txtStatus.setText(status);

                                JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                                String nhietdo = jsonObjectMain.getString("temp");
                                String doam = jsonObjectMain.getString("humidity");

                                Double a = Double.valueOf(nhietdo);
                                String NhietDo = String.valueOf(a.intValue());
                                txtTemp.setText(NhietDo+"'C");
                                txtHumidity.setText(doam+"%");

                                JSONObject jsonObject1Wind = jsonObject.getJSONObject("wind");
                                String gio = jsonObject1Wind.getString("speed");
                                txtWind.setText(gio+"m/s");

                                JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                                String may = jsonObjectClouds.getString("all");
                                txtCloud.setText(may+"%");

                                JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                                String country = jsonObjectSys.getString("country");
                                txtCountry.setText("Ten quoc gia: "+country);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                        }
                    });
            requestQueue.add(stringRequest);

        }
}
