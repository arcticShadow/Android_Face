package com.iceddev.face;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.iceddev.eyesocket.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import static android.text.format.Formatter.formatIpAddress;

public class MainActivity extends Activity implements Orientation.Listener {

  private static final int PORT = 1338;
  private Server server;

  private static final String LOG_TAG = "SensorTest";

  private TextView text;

  private TextView chanText;

  private ImageView viewer;

  private Orientation mOrientation;

  private String lastSent;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    setContentView(R.layout.activity_main);

    text = (TextView) findViewById(R.id.text);
    chanText = (TextView) findViewById(R.id.chan);
    viewer = (ImageView) findViewById((R.id.imageView));
    WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
    WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
    int ip = wifiInfo.getIpAddress();
    final String ipAddress = formatIpAddress(ip);
    chanText.setText("IP: " + ipAddress + ":"+ this.PORT);

    lastSent = "";
    mOrientation = new Orientation((SensorManager) getSystemService(Activity.SENSOR_SERVICE),
            getWindow().getWindowManager());
  }

  @Override
  public void onStart(){
    super.onStart();
    Log.v(LOG_TAG, "started");
  }

  @Override
  protected void onResume() {
    super.onResume();
    server = new Server(PORT, this);
    mOrientation.startListening(this);
  }

  @Override
  protected void onPause() {
    super.onPause();
    server.close();
    mOrientation.stopListening();
  }

  @Override
  public void onOrientationChanged(final float pitch, final float roll) {
    final JSONObject data = new JSONObject();
    try {
      data.put("pitch", Float.valueOf(pitch).intValue());
      data.put("roll", Float.valueOf(roll).intValue());
    } catch (JSONException e) {
      Log.i("JSON Error: ", e.getMessage());
    }
    if (!data.toString().equals(lastSent)) {
      lastSent = data.toString();
      server.sendData(data);
    }
  }

  public void updateViewerPNG(final Bitmap image) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        try {
          if (image != null) {
            viewer.setImageBitmap(image);
          }
        } catch (Exception e) {
          Log.i("Bitmap Error", e.getMessage());
        }
      }
    });
  }
}
