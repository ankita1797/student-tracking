package service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.ankita.studenttracking.ForgotPassword;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import Bean.LocationBean;
import email.GMailSender;
import firebase.FirebaseHelper;

import static android.R.attr.type;

/**
 * Created by Dikshant Manocha on 23-04-2018.
 */
public class MyService extends Service {

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    Geocoder geocoder;
    List<Address> addresses;
    LocationBean previousLocation;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;


        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(final Location location) {
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(previousLocation==null)
            {
                previousLocation=new LocationBean();
                previousLocation.setLocality(addresses.get(0).getSubLocality()+" "+addresses.get(0).getSubAdminArea());
                previousLocation.setPlace(addresses.get(0).getLocality());
                previousLocation.setLat(location.getLatitude());
                previousLocation.setLongi(location.getLongitude());
                SharedPreferences prefs = getSharedPreferences("user_login", MODE_PRIVATE);
                previousLocation.setEnrollment(prefs.getString("enrollment",null));
                previousLocation.setName(prefs.getString("name",null));
                previousLocation.setRoom(prefs.getString("room",null));
                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date=new Date();
                previousLocation.setTime(simpleDateFormat.format(date));
                new FirebaseHelper("student").insertLocation(previousLocation);
            }
            if(!previousLocation.getLocality().equals(addresses.get(0).getSubLocality())){
                previousLocation.setLocality(addresses.get(0).getSubLocality()+" "+addresses.get(0).getSubAdminArea());
                previousLocation.setPlace(addresses.get(0).getLocality());
                previousLocation.setLat(location.getLatitude());
                previousLocation.setLongi(location.getLongitude());
                SharedPreferences prefs = getSharedPreferences("user_login", MODE_PRIVATE);
                previousLocation.setEnrollment(prefs.getString("enrollment",null));
                previousLocation.setName(prefs.getString("name",null));
                previousLocation.setRoom(prefs.getString("room",null));
                SimpleDateFormat simpleDateFormat =new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date=new Date();
                previousLocation.setTime(simpleDateFormat.format(date));
                new FirebaseHelper("student").insertLocation(previousLocation);


            }
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }



    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
