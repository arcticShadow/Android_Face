package com.iceddev.face;

import android.util.Log;

import com.iceddev.eyesocket.Server;
import com.iceddev.utls.FunUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Data {

    private Server server;

    private JSONObject buttons;

    private JSONObject gestures;

    private HashMap<String, Float> orientation;

    private HashMap<String, Float> initialOrientation;

    private ARDroneDirection arDirection;

    public Data(Server server){
        this.server = server;
        this.arDirection = new ARDroneDirection();

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
            data.put("direction", this.arDirection.toJSON());
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


    public void setInitialOrientation(HashMap<String, Float> initialOrientation) {
        Log.i("INTIALPITCH", initialOrientation.get("pitch").toString());
        this.initialOrientation = initialOrientation;
    }

    public void setOrientation(HashMap<String, Float> orientation) {
        boolean changed = true;

        try {
            changed = this.orientationChange(orientation);
        } catch(NullPointerException e) {
            Log.i("NOTHING TO WORRY", "Caught a NPE, but don't worry, we expected it and caught it");
        }

        if(changed) {
            this.orientation = orientation;
            this.setARDirections(orientation);
            this.server.sendData(this);
        }

    }

    private void setARDirections(HashMap<String, Float> oldOrientation) {
        this.setARDirectionPitch(oldOrientation);
        this.setARDirectionAzimuth(oldOrientation);
        this.setARDirectionRoll(oldOrientation);
    }
    private void setARDirectionPitch(HashMap<String, Float> oldOrientation) {
        Float pitch = this.orientation.get("pitch");
        Float initialPitch = initialOrientation.get("pitch");

        if(pitch > initialPitch) {
            this.arDirection.setForward(1);
        } else {
            this.arDirection.setForward(0);
        }
        if (pitch <= initialPitch) {
            this.arDirection.setBackward(1);
        } else {
            this.arDirection.setBackward(0);
        }
    }
    private void setARDirectionAzimuth(HashMap<String, Float> oldOrientation) {
        Float azimuth = this.orientation.get("azimuth");
        Float oldAzimuth = initialOrientation.get("azimuth");

        if(azimuth > oldAzimuth) {
//            this.arDirection.setForward(1);
        } else if (azimuth < oldAzimuth) {
//            this.arDirection.setBackward(1);
        }
    }

    private void setARDirectionRoll(HashMap<String, Float> oldOrientation) {
        Float roll = this.orientation.get("roll");
        Float initialRoll = initialOrientation.get("roll");

        if(roll > initialRoll) {
//            this.arDirection.setForward(1);
        } else if (roll < initialRoll) {
//            this.arDirection.setBackward(1);
        }
    }

    private boolean orientationChange(HashMap<String, Float> orientation) {
        // Variance is the amount to wait for, before updating the values and sending them out the websocket.
        int variance = 10;

        if(!FunUtils.numberTollerance(orientation.get("azimuth"), this.orientation.get("azimuth"), variance)){
            return true;
        }

        if(!FunUtils.numberTollerance(orientation.get("pitch"), this.orientation.get("pitch"), variance)){
            return true;
        }
        // This used to be an 'if', But this is the asme as what i had, but with a few lines less code.
        if(!FunUtils.numberTollerance(orientation.get("roll"), this.orientation.get("roll"), variance)){
            return true;
        }
        return false;

    }
}
