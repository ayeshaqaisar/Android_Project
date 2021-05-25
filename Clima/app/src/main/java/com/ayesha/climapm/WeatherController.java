 package com.ayesha.climapm;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayesha.climapm.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


 public class WeatherController extends AppCompatActivity {

    final int REQUEST_CODE = 12;
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    // App ID to use OpenWeather data
    final String APP_ID = "bef61b3c4b73b6494bbab8143df4f469";
    // Time between location updates (5000 milliseconds or 5 seconds)
    final long MIN_TIME = 5000;
    // Distance between location updates (1000m or 1km)
    final float MIN_DISTANCE = 1000;

    String LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
    LocationManager mLocationManager;
    LocationListener mLocationListener;


    // Member Variables:
    TextView mCityLabel;
    ImageView mWeatherImage;
    TextView mTemperatureLabel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_controller_layout);

        // Linking the elements in the layout to Java code
        mCityLabel = (TextView) findViewById(R.id.locationTV);
        mWeatherImage = (ImageView) findViewById(R.id.weatherSymbolIV);
        mTemperatureLabel = (TextView) findViewById(R.id.tempTV);
        ImageButton changeCityButton = (ImageButton) findViewById(R.id.changeCityButton);

        changeCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(WeatherController.this, ChangeCityController.class);
                startActivity(myIntent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Clima","onResume() called");

        Intent myIntent =getIntent();
        String city = myIntent.getStringExtra("City");

        //will give the info about weather at current location if no city is provided
        if(city!=null){
            getWeatherForNewCity(city);
        }else{
            getWeatherForCurrentLocation();
        }





    }


    // When city is provided
    private void getWeatherForNewCity(String city){
        Log.d("Clima","getWeatherForNewCity() called");
        RequestParams params = new RequestParams();

        params.put("q",city);
        params.put("appid",APP_ID);
        letsDoSomeNetworking(params);


    }


    // When city is not provided current location weather is sent
    private void getWeatherForCurrentLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Clima", "onLocationChanged() call back received");

                String longitude =String.valueOf(location.getLongitude());
                String latitude =String.valueOf(location.getLatitude());

                RequestParams params = new RequestParams();

                params.put("lat",latitude);
                params.put("lon",longitude);
                params.put("appid",APP_ID);
                letsDoSomeNetworking(params);


            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Log.d("Clima", "onProviderDisabled() call back received");


            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {



            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(LOCATION_PROVIDER, MIN_TIME, MIN_DISTANCE, mLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 &&  grantResults[0]  == PackageManager.PERMISSION_GRANTED){
                Log.d("Clima","onRequestPermissionsResult(): Permission granted!");
                getWeatherForCurrentLocation();
            }
            else{
                Log.d("Clima","Permission denied");
            }

        }

    }


    private void letsDoSomeNetworking(RequestParams params){

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(WEATHER_URL, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("Clima", "Success! JSON: "+response.toString());

                WeatherDataModel weatherData = WeatherDataModel.fromJson(response);
                updateUI(weatherData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("Clima","Fail "+ throwable.toString() );
                Log.d("Clima" ,"Status Code "+ statusCode);
                Toast.makeText(WeatherController.this,"Request Failed",Toast.LENGTH_SHORT).show();

            }
        });
    }

     private void updateUI(WeatherDataModel weather){

         mTemperatureLabel.setText(weather.getTemperature());
         mCityLabel.setText(weather.getCity());

         int resourceID =getResources().getIdentifier(weather.getIconName(), "drawable",getPackageName());
         mWeatherImage.setImageResource(resourceID);

     }



  ///To free up space when the app is not running  onPause method is called

     @Override
     protected void onPause() {
         super.onPause();
         //To stop getting Updates
         if(mLocationManager != null){
             mLocationManager.removeUpdates(mLocationListener);
         }
     }
 }
