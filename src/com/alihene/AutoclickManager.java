package com.alihene;

public class AutoclickManager implements Runnable {
    static volatile double MillisDelay = 0;
    static volatile int TypeOfClick;

    @Override
    public void run() {
        System.out.println("Autoclicker mnager is Working");
    }
}
