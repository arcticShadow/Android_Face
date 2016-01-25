package com.iceddev.utls;

/**
 * Created by coledi on 25/01/16.
 */
public class FunUtils {
    public static boolean intBetween(int x, int min, int max)
    {
        return intBetween(new Float(x), new Float(min) , new Float(max));
    }

    public static boolean intBetween(float x, float min, float max)
    {
        return x > min && x < max;
    }

    /**
     *
     * @param num1 value to check
     * @param num2 value to apply tollarnce to
     * @param tollarance variance value to apply to num2, when checking num1
     * @return boolean
     */
    public static boolean numberTollerance(float num1, float num2, int tollarance){
        return intBetween(num1, num2 - tollarance, num2 + tollarance);
    }

    /**
     *
     * @param num1 value to check
     * @param num2 value to apply tollarnce to
     * @param tollarance variance value to apply to num2, when checking num1
     * @return boolean
     */
    public static boolean numberTollerance(int num1, int num2, int tollarance){
        return intBetween(num1, num2 - tollarance, num2 + tollarance);
    }
}
