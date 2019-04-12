package com.unipi.cbarbini.unipimeter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ntt.customgaugeview.library.GaugeView;

public class SpeedOmeterActivity extends AppCompatActivity implements LocationListener {
    Button btn_myaccount,btn_start,btn_stop;
    TextView txt;
    GaugeView gaugeView;

    double distance=0.0;
    double Kmspeed=0.0;

    boolean dialogShown=false;
    boolean proxalert=false;
    boolean entering=false;

    double loclat=0.0;
    double loclong=0.0;
    String [][] array_of_locations;
    String[] visited_location;
    int speedlimit=0;
    int R_value=0;

    SharedPreferences preferences;
    LocationManager locationManager;
    DatabaseHandler db_object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_ometer);
        btn_myaccount =findViewById(R.id.button3);
        btn_start=findViewById(R.id.button6);
        btn_stop=findViewById(R.id.button8);
        txt=findViewById(R.id.textView3);

        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        db_object=new DatabaseHandler(getApplicationContext());
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        txt.setText("?");
        //load LOCATIONS table
        array_of_locations=db_object.array_of_locations();

        //graphical speedometer
        gaugeView = (GaugeView) findViewById(R.id.gauge_view);
        gaugeView.setShowRangeValues(true);
        gaugeView.setTargetValue(0);

        btn_myaccount.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyAccountActivity.class));
            }
        }));

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onLocationChanged(Location location) {
        //call getspeed(location) to update speed and current location
         getspeed(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void go (View view)
    {
        init();

        //check if gps service is enabled
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)==true)
        {
            //user permission ACCESS FINE LOCATION because we use gps provider
            //if user has already given this permission Call onlocationChnaged via location manager else call requestStoragePermission();
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                btn_start.setVisibility(Button.INVISIBLE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);   }
            else requestStoragePermission();}

        else
        {Toast.makeText(this, "OPEN GPS LOCATION SERVICES FIRST", Toast.LENGTH_SHORT).show();}
    }

    private void requestStoragePermission() {
        //ask for permissions
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {warnings(3,"");}
        else {ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1); }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        //runtime permissions call back
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults [0]==PackageManager.PERMISSION_GRANTED)
        {  if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED )
            {   Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                btn_start.setVisibility(Button.INVISIBLE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);}
        }
        else Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();

    }
    public void stop (View view)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED )
        {
            //stop updates but first check for user's permissions
            locationManager.removeUpdates(this);
            gaugeView.setTargetValue(0);
            btn_start.setVisibility(Button.VISIBLE);
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //two ways of calculating current speed .Sometimes location.currentspeed isnt that accurate
    //distance between two location last location and current gives us more precise results
    private void getspeed(Location location){
        double newLat = location.getLatitude();
        double newLon = location.getLongitude();
        //txt2.setText(String.valueOf(newLat)+","+String.valueOf(newLon));
        if(location.hasSpeed() && location.getSpeed()>0)
        {
            float speed = location.getSpeed();
            Kmspeed = speed* 3.6;
            checkspeed(Kmspeed,newLat,newLon);

        }
        //proximity alert
        if(entering==false)checkdistance_entering(newLat,newLon);
        if(entering==true)checkdistance_exiting(newLat,newLon);

    }

    public void checkspeed(double Kmspeed,double lat,double lon)
    {    if(Kmspeed >= speedlimit && dialogShown==false)
            {   warnings(1,"");
                dialogShown=true;
                //insert into db
                db_object.insertSpeedStat(preferences.getString("username", "examples@example.com"),String.valueOf(Kmspeed),String.valueOf(speedlimit),String.valueOf(lat)+","+String.valueOf(lon));
            }
         if(Kmspeed<speedlimit)dialogShown=false;

            gaugeView.setTargetValue((float)Kmspeed);
    }

    public void checkdistance_entering(double lat,double lon)
    {
             for(int i=0; i<array_of_locations.length; i++)
             {
                //split location into x ,y
                visited_location= array_of_locations[i][1].split(",");
                //calculate distance
                distance=distance(lat,lon,Double.valueOf(visited_location[0]),Double.valueOf(visited_location[1]));

                 if (distance <= R_value*0.001 && proxalert==false)
                {
                    //near a point of interest
                    entering=true;
                    loclat=Double.valueOf(visited_location[0]);
                    loclong=Double.valueOf(visited_location[1]);
                    if(entering==true)
                    {   //visited once ,enter
                        proxalert=true;
                        warnings(2,array_of_locations[i][0]);
                        db_object.insertLocStat(preferences.getString("username", "examples@example.com"),array_of_locations[i][0],String.valueOf(lat)+","+String.valueOf(lon));
                        break;
                    }
                }else distance=0.0;
            }

    }
    public void checkdistance_exiting(double lat,double lon)
    {
        distance=distance(lat,lon,loclat,loclong);
            if (distance > R_value*0.001)
            {   entering=false;
                proxalert=false;
            }
    }
     //////////////////////////////////////////////////////////////////////////////////////////

     //calculate distance given coordinates
      public double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist; // output distance, in km }
    }
      //warning alertdialog builder for all cases
      public void warnings(int warningnumber,String location)
      { AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning");
        builder.setCancelable(true);
        builder.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        if (warningnumber==1)
        {
            builder.setMessage("Exceeded Speed Limit!Slow down for your own safety :) !");
            builder.setIcon(R.drawable.warning1);
        }
        else if (warningnumber==2){
            builder.setMessage("You are near "+location +"! Go to myaccount to check locations Records!");
            builder.setIcon(R.drawable.warning2);
        }
        else {
            builder.setMessage("We need this permission to begin !");
            builder.setIcon(R.drawable.permission);
        }
        AlertDialog alertbuilder = builder.create();
        alertbuilder.show();}

        //initialize values
        public void init()
        {
           dialogShown=false;
           proxalert=false;
           entering=false;
           loclat=0.0;
           loclong=0.0;
           distance=0.0;
           Kmspeed=0.0;
           speedlimit=Integer.valueOf(preferences.getString("speedlimit", "40"));
           R_value=Integer.valueOf(preferences.getString("R", "4"));
           txt.setText(preferences.getString("speedlimit", "40"));
        }

}
