package com.alihene;

import java.awt.*;
import java.awt.event.InputEvent;

@SuppressWarnings({"deprecation"})
public class Autoclick implements Runnable {

    static Robot Clicker;

    Autoclick(){
        try {
            Clicker = new Robot();
        } catch (AWTException er) {
            er.printStackTrace();
        }
    }

    @Override
    public void run() {
        AutoclickManager.TypeOfClick = 1;
        for(; ;) {
            Click(AutoclickManager.MillisDelay, AutoclickManager.TypeOfClick);
        }
    }

    //Click method, checks the type of click and interval and clicks according to those


    public void Click(double Delay, int Type){
        if(Main.ToggleClick){
            if(Type == 1) {
                Clicker.mousePress(InputEvent.BUTTON1_MASK);
                Clicker.mouseRelease(InputEvent.BUTTON1_MASK);
                Clicker.delay((int) Math.ceil(Delay));
            }else if(Type == 2){
                Clicker.mousePress(InputEvent.BUTTON3_MASK);
                Clicker.mouseRelease(InputEvent.BUTTON3_MASK);
                Clicker.delay((int) Math.ceil(Delay));
            }
        }
    }
}
