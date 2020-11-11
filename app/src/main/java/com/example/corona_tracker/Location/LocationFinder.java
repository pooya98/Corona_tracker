package com.example.corona_tracker.Location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationFinder {
    Context context;
    LocationManager lm;
    private static final double LOCATION_ERROR = 200;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    Geocoder geocoder;
    Location here;
    public LocationFinder(Context c) {
        context = c;
        lm = (LocationManager)context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context, Locale.getDefault());
    }

    public boolean checkAvailable(){
        if(!isLocationServiceOn()){
            showLocationServiceDialog();
        }
        else getRunTimePermission();

        if(checkRunTimePermission() && isLocationServiceOn()) return true;
        else return false;
    }

    public double getLatitude(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
        else return LOCATION_ERROR;
    }

    public double getLongitude(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
        else return LOCATION_ERROR;
    }

    public String getAddress(){
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        try {
            if((here = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)) == null) {
                here = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Log.i("mTag", "here");
            }

            List<Address> result = geocoder.getFromLocation(here.getLatitude(), here.getLongitude(), 3);
            return result.get(0).getAddressLine(0);
        } catch (IOException e) {
            return null;
        } catch (NullPointerException e){
            return null;
        }
        return null;
    }

    private boolean isLocationServiceOn(){
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showLocationServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("위치 서비스 활성화");
        builder.setMessage("위치를 읽기 위해서는 위치서비스를 켜야 합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(i);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "위치서비스를 활성화해주세요", Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();
    }

    private void getRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, REQUIRED_PERMISSIONS[0])) {
                ActivityCompat.requestPermissions((Activity)context, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
            else {
                ActivityCompat.requestPermissions((Activity)context, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private boolean checkRunTimePermission(){
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        else return true;
    }
}
