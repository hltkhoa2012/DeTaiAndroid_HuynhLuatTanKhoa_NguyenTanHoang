package com.example.detai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
String tenthanhpho="";
ImageView imgBack;
TextView txtName;
ListView lv;
CustomAdapter customAdapter;
ArrayList<Thoitiet> mangthoitiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Anhxa();
        Intent intent=getIntent();
        String city=intent.getStringExtra("name");
        Log.d("ketqua","Dữ liệu truyền qua : "+city);
       if (city.equals("")){
           tenthanhpho = "Hanoi";
           Get7DayData(tenthanhpho);
       }else {
           tenthanhpho=city;
           Get7DayData(tenthanhpho);
       }
       imgBack.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });
    }

    private void Anhxa() {
        imgBack=(ImageView)findViewById(R.id.ImageviewBack);
        txtName=(TextView)findViewById(R.id.textviewTenthanhpho);
        lv=(ListView)findViewById(R.id.Listview);
        mangthoitiet=new ArrayList<Thoitiet>();
        customAdapter=new CustomAdapter(Main2Activity.this,mangthoitiet);
        lv.setAdapter(customAdapter);
    }

    private void Get7DayData(String data) {
        String url="https://samples.openweathermap.org/data/2.5/forecast/daily?q="+data+"&appid=a9baa7bc01541882d0e1aea14e67012d";
        //String url="http://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=a9baa7bc01541882d0e1aea14e67012d";
        Log.d("NeroSciti1",url);


        RequestQueue requestQueue= Volley.newRequestQueue(Main2Activity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject1 =new JSONObject(response);

                            JSONArray jsonArrayList=jsonObject1.getJSONArray("list");
                            for (int i=0;i<jsonArrayList.length();i++){
                                JSONObject jsonObjectList=jsonArrayList.getJSONObject(i);

                                String ngay=jsonObjectList.getString("dt");
                                Long l = Long.valueOf(ngay);
                                Date date = new Date(l * 1000L);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd-MM-yyyy ");
                                String Day = simpleDateFormat.format(date);

                                JSONObject jsonObjectTemp=jsonObjectList.getJSONObject("temp");
                                String max=jsonObjectTemp.getString( "max");
                                String min=jsonObjectTemp.getString("min");

                                /*JSONObject jsonObjectTemp=jsonObjectList.getJSONObject("main");
                                String max=jsonObjectTemp.getString( "temp_max");
                                String min=jsonObjectTemp.getString("temp_min");*/

                                Double a = Double.valueOf(max);
                                Double b = Double.valueOf(min);
                                String NhietDoMax = String.valueOf(a.intValue());
                                String NhietDoMin = String.valueOf(b.intValue());

                                JSONArray jsonArrayWeather=jsonObjectList.getJSONArray("weather");
                                JSONObject jsonObjectWeather=jsonArrayWeather.getJSONObject(0);
                                String status=jsonObjectWeather.getString( "description");
                                String icon= jsonObjectWeather.getString("icon");

                                /*JSONObject jsonObjectWeather = jsonObjectList.getJSONObject( "weather");
                                String status=jsonObjectWeather.getString( "description");
                                String icon= jsonObjectWeather.getString("icon");*/

                                mangthoitiet.add(new Thoitiet(Day,status,icon,NhietDoMax,NhietDoMin));

                                JSONObject jsonObjectCity=jsonObject1.getJSONObject("city");
                                String name = jsonObjectCity.getString("name");
                                txtName.setText(name);

                            }
                            customAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }
}
