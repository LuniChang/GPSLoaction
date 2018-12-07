package com.xu.gps;

import android.content.Context;
import android.icu.util.TimeUnit;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.ExecutorService;


public class GPSLoactionPlugin extends CordovaPlugin {

    private static final String ACTION_GETLOCATION = "getlocation";

    private static final String ACTION_GETLOCATION_ALL_TIME = "getlocationalltime";

    private static final String ACTION_GETLOCATION_STOP = "stop";

    private static final String TAG = "GPSLoactionPlugin";

    private LocationManager locationManager = null;
    private Criteria locationOption = null;
    private GpsLocationListener locationListener = new GpsLocationListener();
    private CallbackContext callbackContext = null;

    private Context context;
    private boolean isOnceLocation = true;
    private long interval = 1000L;

    private long timeOutSet = 10000L;

    private boolean hadGetGps=false;
    private long curentGetGpsTime=0;

    private boolean isWatchTimeOut=false;

    private ExecutorService executorService;


    protected class  WatchTimeOutRunnable implements Runnable{
        @Override
        public void run() {
           while (isWatchTimeOut){


               try {
                   Thread.sleep(timeOutSet);
               } catch (InterruptedException e) {
                   Log.e(TAG, "isWatchTimeOut error");
               }

               if(!hadGetGps&&curentGetGpsTime+timeOutSet<System.currentTimeMillis()){

                   cordova.getActivity().runOnUiThread(new Runnable() {
                       public void run() {

                           whenTimeOut();
                       }
                   });


               }

           }
        }
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        context = this.cordova.getActivity().getApplicationContext();

        super.initialize(cordova, webView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean execute(String action, JSONArray args,
                           CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;

        this.hadGetGps=false;


        Log.i(TAG, action);
        if (ACTION_GETLOCATION.equals(action.toLowerCase(Locale.CHINA))) {

            try {
                timeOutSet = Long.valueOf(args.getInt(0));
            } catch (Exception e) {
                Log.e(TAG, "timeOutSet error");
            }
            stopLocation();
            isOnceLocation = true;
            startLocation(context);
            startWatchTimeOut();
            return true;
        } else if (ACTION_GETLOCATION_ALL_TIME.equals(action.toLowerCase(Locale.CHINA))) {

            try {
                interval = Long.valueOf(args.getInt(0));
            } catch (Exception e) {
                Log.e(TAG, "interval error");
            }
            try {
                timeOutSet = Long.valueOf(args.getInt(1));
            } catch (Exception e) {
                Log.e(TAG, "timeOutSet error");
            }
            isOnceLocation = false;
            startLocation(context);
            startWatchTimeOut();

            return true;
        } else if (ACTION_GETLOCATION_STOP.equals(action.toLowerCase(Locale.CHINA))) {
            isOnceLocation = true;
            stopLocation();
            return true;
        }
        return true;
    }

    public void startWatchTimeOut(){
        if(this.isWatchTimeOut==true){
            return;
        }
        this.isWatchTimeOut=true;

        this.curentGetGpsTime=System.currentTimeMillis();

        if(executorService!=null)
            executorService.shutdown();

        executorService=cordova.getThreadPool();
        executorService.execute(new WatchTimeOutRunnable());


    }

    private void stopLocation() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        }
        if (locationListener != null)
            locationManager.removeUpdates(locationListener);

        if(executorService!=null)
           executorService.shutdown();
         isWatchTimeOut=false;

    }


    protected void whenTimeOut(){
        Log.i(TAG,"gps timeout");
        JSONObject jo = new JSONObject();
        try {

            jo.put("is_timeout", 1);

            Log.e(TAG, "json:" + jo.toString());
        } catch (JSONException e) {
            jo = null;
            e.printStackTrace();
        }


        if (!isOnceLocation) {

            PluginResult r = new PluginResult(PluginResult.Status.OK, jo);

            r.setKeepCallback(true);

            callbackContext.sendPluginResult(r);

        } else {
            callbackContext.success(jo);
            stopLocation();
        }
    }


    protected void callbackLocation(android.location.Location location) {
        if (location != null) {
            hadGetGps=true;
            this.curentGetGpsTime=System.currentTimeMillis();
            // 获取位置信息
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();
            boolean hasAccuracy = location.hasAccuracy();
            float accuracy = location.getAccuracy();

            // 速度
            float speed = location.getSpeed();
            // 角度
            float bearing = location.getBearing();
            // 星数
            int satellites = location.getExtras().getInt("satellites", 0);
            // 时间
            long time = location.getTime();

            JSONObject jo = new JSONObject();
            try {
                jo.put("latitude", latitude);
                jo.put("longitude", longitude);
                jo.put("hasAccuracy", hasAccuracy);
                jo.put("accuracy", accuracy);
                jo.put("is_timeout", 0);
                jo.put("speed", speed);
                jo.put("bearing", bearing);
                jo.put("satellites", satellites);
                jo.put("time", time);
                Log.e(TAG, "json:" + jo.toString());
            } catch (JSONException e) {
                jo = null;
                e.printStackTrace();
            }


            if (!isOnceLocation) {

                PluginResult r = new PluginResult(PluginResult.Status.OK, jo);

                r.setKeepCallback(true);

                callbackContext.sendPluginResult(r);

            } else {
                callbackContext.success(jo);
                stopLocation();
            }

        }
    }

    public class GpsLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(android.location.Location location) {
            callbackLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            callbackContext.error(provider);
        }

    }

    public void startLocation(Context context) {
        if (locationManager == null)
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationOption == null) {
            locationOption = new Criteria();
            locationOption.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
            locationOption.setAltitudeRequired(false);
            locationOption.setBearingRequired(false);
            locationOption.setCostAllowed(true);
            locationOption.setPowerRequirement(Criteria.POWER_LOW); // 功耗
        }


        try {




            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, interval, 1,
                    locationListener);
        } catch (Exception e) {

            callbackContext.error(e.getMessage());
        }

    }


}
