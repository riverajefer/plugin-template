package com.example;

import android.Manifest;
import org.apache.cordova.CallbackContext;
import android.content.pm.PackageManager;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import android.content.Context;
import org.apache.cordova.PluginResult;
import org.apache.cordova.PluginResult.Status;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.util.Log;
import java.util.Date;
import io.chirp.connect.ChirpConnect;
import io.chirp.connect.interfaces.ConnectEventListener;
import io.chirp.connect.interfaces.ConnectLicenceListener;
import io.chirp.connect.models.ChirpError;
import io.chirp.connect.models.ConnectState;

public class ChirpPlugin extends CordovaPlugin {

  private static final String TAG = "ChirpPlugin";
  private ChirpConnect chirpConnect;
  private static final int RESULT_REQUEST_RECORD_AUDIO = 1;
  private static final String LISTEN = "listen";
  private static CallbackContext listener;
  public static final String RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);

    if (cordova.hasPermission(RECORD_AUDIO)) {
      initChirp();
      chirpListener();
    }

  }

  private void initChirp() {

    String APP_KEY = "0UMHyXAjqyWjWPl1Z5FeNtj6C";
    String APP_SECRET = "anr2ibeMxYx9MsYQcvEOZ51w6LVAK97SCMZFf22s1o0giHN2oA";

    chirpConnect = new ChirpConnect(cordova.getActivity(), APP_KEY, APP_SECRET);

    chirpConnect.getLicence(new ConnectLicenceListener() {

      @Override
      public void onSuccess(String licence) {
        ChirpError licenceError = chirpConnect.setLicence(licence);
        if (licenceError.getCode() > 0) {
          Log.e("setLicenceError", licenceError.getMessage());
          return;
        }
      }

      @Override
      public void onError(ChirpError chirpError) {
        Log.e("getLicenceError", chirpError.getMessage());
      }

    });
  }

  private void chirpListener() {

    chirpConnect.setListener(new ConnectEventListener() {

      @Override
      public void onSending(byte[] data) {
      }

      @Override
      public void onSent(byte[] data) {
      }

      @Override
      public void onReceiving() {
        Log.v("connectdemoapp", "ConnectCallback: onReceiving");
      }

      @Override
      public void onReceived(byte[] data) {
        String hexData = "null";
        if (data != null) {
          hexData = bytesToHex(data);
        }
        Log.v("connectdemoapp", "ConnectCallback: onReceived: " + hexData);
        updateLastPayload(hexData);
      }

      @Override
      public void onStateChanged(byte oldState, byte newState) {
        Log.v("connectdemoapp", "ConnectCallback: onStateChanged " + oldState + " -> " + newState);
      }

      @Override
      public void onSystemVolumeChanged(int oldVolume, int newVolume) {
      }
    });
  }

  public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {

    Log.i(TAG, "Received action " + action);

    if (action.equals("start")) {
      if (cordova.hasPermission(RECORD_AUDIO)) {
      } else {
        getReadPermission(RESULT_REQUEST_RECORD_AUDIO);
      }
    }

    if (action.equals("listen")) {
      if (cordova.hasPermission(RECORD_AUDIO)) {
        startSdk();
      } else {
        getReadPermission(RESULT_REQUEST_RECORD_AUDIO);
      }

      final PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
      result.setKeepCallback(true);
      callbackContext.sendPluginResult(result);
      setListener(callbackContext);
    }

    if (action.equals("stop")) {
      Log.d(TAG, "STOP SDK CHIRP");
      stopSdk();
      final PluginResult result = new PluginResult(PluginResult.Status.OK);
      callbackContext.sendPluginResult(result);
    }

    return true;
  }

  public void setListener(CallbackContext callbackContext) {
    listener = callbackContext;

    PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
    result.setKeepCallback(true);
    callbackContext.sendPluginResult(result);
  }

  protected void getReadPermission(int requestCode) {
    cordova.requestPermission(this, requestCode, RECORD_AUDIO);
  }

  public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
    Log.v(TAG, "onRequestPermissionsResult");
    switch (requestCode) {
    case RESULT_REQUEST_RECORD_AUDIO: {
      if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        Log.d(TAG, "PERMISSION_GRANTED STARTSDK");
        initChirp();
        chirpListener();
        startSdk();
      } else {
        try {
          PluginResult result = new PluginResult(PluginResult.Status.ERROR, "PERMISSION_DENIED_ERROR");
          listener.sendPluginResult(result);
        } catch (Exception e) {
        }
      }
      return;
    }
    }
  }

  public void startSdk() {
    if (chirpConnect.getConnectState() == ConnectState.AudioStateStopped) {
      ChirpError error = chirpConnect.start();
      Log.d(TAG, "START SDK CHIRP>>>");
      if (error.getCode() > 0) {
        Log.e("ConnectError: ", error.getMessage());
        return;
      }
    }
  }

  public void stopSdk() {
    ChirpError error = chirpConnect.stop();
    if (error.getCode() > 0) {
      Log.e("ConnectError: ", error.getMessage());
      return;
    }
  }

  public void updateLastPayload(final String newPayload) {
    cordova.getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Log.e("newPayload: ", newPayload);

        PluginResult result = new PluginResult(PluginResult.Status.OK, newPayload);
        result.setKeepCallback(true);
        listener.sendPluginResult(result);
      }
    });
  }

  public static String bytesToHex(byte[] in) {
    final StringBuilder builder = new StringBuilder();
    for (byte b : in) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }

}
