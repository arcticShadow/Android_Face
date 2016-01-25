package com.iceddev.utls;

import java.util.HashMap;


public class ARDroneDefaultHashMap extends HashMap<String, Float> {
    protected Float defaultValue;
    public ARDroneDefaultHashMap(Float defaultValue) {
        this.defaultValue = defaultValue;
    }
    public ARDroneDefaultHashMap() {
        this.defaultValue = new Float(0);
    }

    public Float get(Float k) {
        return containsKey(k) ? super.get(k) : defaultValue;
    }
}