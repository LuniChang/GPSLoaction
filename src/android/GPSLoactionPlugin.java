package com.xu.gps;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
        Log.e(TAG, action);
        if (ACTION_GETLOCATION.equals(action.toLowerCase(Locale.CHINA))) {
            stopLocation();
            isOnceLocation = true;
            startLocation(context);
            return true;
        } else if (ACTION_GETLOCATION_ALL_TIME.equals(action.toLowerCase(Locale.CHINA))) {

            try {
                interval = Long.valueOf(args.getInt(0));
            } catch (Exception e) {
                Log.e(TAG, "interval error");
            }

            isOnceLocation = false;
            startLocation(context);
            return true;
        } else if (ACTION_GETLOCATION_STOP.equals(action.toLowerCase(Locale.CHINA))) {
            isOnceLocation = true;
            stopLocation();
            return true;
        }
        return true;
    }

    private void stopLocation() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        }
        if (locationListener != null)
            locationManager.removeUpdates(locationListener);
    }


    protected void callbackLocation(android.location.Location location) {
        if (location != null) {
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
            locationOption.setPowerRequirement(Criteria.POWER_HIGH); // 低功耗
        }


        String provider = locationManager.getBestProvider(locationOption, true); // 获取GPS信息
        try {
            if (isOnceLocation) {
                Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置

                if(location!=null){
                    callbackLocation(location);
                    return;
                }


            }


            // 设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
            locationManager.requestLocationUpdates(provider, interval, 1,
                    locationListener);
        } catch (Exception e) {

            callbackContext.error(e.getMessage());
        }

    }


}
