package com.iceddev.face;

import android.util.Log;

import com.iceddev.utls.ARDroneDefaultHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by coledi on 25/01/16.
 */
public class ARDroneDirection {
    private float left;
    private float right;
    private float up;
    private float down;
    private float forward;
    private float backward;
    private float counterClockwise;
    private float clockwise;

    ARDroneDirection() {
        this.left = 0;
        this.right = 0;
        this.up = 0;
        this.down = 0;
        this.forward = 0;
        this.backward = 0;
        this.counterClockwise = 0;
        this.clockwise = 0;
    }

    ARDroneDirection(ARDroneDefaultHashMap directions) {
        this.left = directions.get("left");
        this.right = directions.get("right");
        this.up = directions.get("up");
        this.down = directions.get("down");
        this.forward = directions.get("forward");
        this.backward = directions.get("backward");
        this.counterClockwise = directions.get("counterClockwise");
        this.clockwise = directions.get("clockwise");
    }

    public void setLeft(float left){
        this.right = 0;
        this.left = left;
    }
    public void setRight(float right){
        this.left = 0;
        this.right = right;
    }
    public void setUp(float up){
        this.down = 0;
        this.up = up;
    }
    public void setDown(float down){
        this.up = 0;
        this.down = down;
    }
    public void setForward(float forward){
        this.backward = 0;
        this.forward = forward;
    }
    public void setBackward(float backward){
        this.forward = 0;
        this.backward = backward;
    }
    public void setCounterClockwise(float counterClockwise){
        this.clockwise = 0;
        this.counterClockwise = counterClockwise;
    }
    public void setClockwise(float clockwise){
        this.counterClockwise = 0;
        this.clockwise = clockwise;
    }

    @Override
    public String toString() {
        JSONObject data = new JSONObject();
        try {
            data.put("direction", this);
        } catch (JSONException e) {
            Log.e("JSON ERROR", "in toString: " + e.toString());
        }
        return data.toString();
    };





}
