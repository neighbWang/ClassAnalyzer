package com.liuyun;

public class TestClass implements Cloneable {

    public static final String TAG = "liuyun";
    private int num;

    public void inc() {
        num++;
    }

    public int exception() {
        int x;
        try {
            x = 1;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }

    public static void showTag() {
        System.out.println(TAG);
    }


}