package com.iceddev.face;

import android.util.Log;

import com.iceddev.eyesocket.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Data {

    private Server server;

    private JSONObject buttons;

    private JSONObject gestures;

    private HashMap<String, Float> orientation;

    public Data(Server server){
        this.server = server;

        buttons = new JSONObject();
        gestures = new JSONObject();
        try {
            buttons.put("A", false);
            buttons.put("B", false);

            gestures.put("tap", false);
            gestures.put("scrollLeft", false);
            gestures.put("scrollRight", false);
            gestures.put("down", false);
            gestures.put("longPress", false);
            gestures.put("doubleTap", false);
        } catch (JSONException e) {
            Log.e("JSON ERROR", "in constructor: " + e.toString());
        }
    }


    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public JSONObject getButtons() {
        return buttons;
    }

    public void setButton(String button, boolean pressed) {
        try {
            this.buttons.put(button, pressed);
            this.server.sendData(this);
            this.buttons.put(button, !pressed);
        } catch (JSONException e) {
            Log.e("JSON ERROR", "in setButton: " + e.toString());
        }
    }

    @Override
    public String toString() {
        JSONObject data = new JSONObject();
        try {
            data.put("buttons", this.buttons);
            data.put("gestures", this.gestures);
            data.put("orientation", new JSONObject(this.orientation));
        } catch (JSONException e) {
            Log.e("JSON ERROR", "in toString: " + e.toString());
        }
        return data.toString();
    }

    public JSONObject getGestures() {
        return gestures;
    }

    public void setGesture(String gesture, boolean made) {
        try {
            this.gestures.put(gesture, made);
            this.server.sendData(this);
            this.gestures.put(gesture, !made);
        } catch (JSONException e) {
            Log.e("JSON ERROR", "in setGesture: " + e.toString());
        }
    }

//  public JSONObject getOrientation() {
//    return orientation;
//  }

    public void setOrientation(HashMap<String, Float> orientation) {
        if(this.orientationChange(orientation)) {
            this.orientation = orientation;
            // TODO: create/update instance of ARDroneDirection
            this.server.sendData(this);
        }

    }
    private boolean orientationChange(HashMap<String, Float> orientation) {
        // TODO: Override this to round the float values, limiting the data that will be sent via websocket
        return this.orientation.equals(orientation);
    }
}
