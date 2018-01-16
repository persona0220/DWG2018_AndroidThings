package com.google.androidthings.education.mtg;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.contrib.driver.ht16k33.AlphanumericDisplay;
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat;
import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

public class ButtonABC {

    private static final String TAG = "Button";

    private Button buttonA;
    private Button buttonB;
    private Button buttonC;

    public boolean open() {
        try {
            buttonA = RainbowHat.openButtonA();
            buttonB = RainbowHat.openButtonB();
            buttonC = RainbowHat.openButtonC();

            buttonA.setOnButtonEventListener(new Button.OnButtonEventListener() {
                @Override
                public void onButtonEvent(Button button, boolean pressed) {
                    Log.d(TAG, "button A pressed:" + pressed);
                }
            });
            buttonB.setOnButtonEventListener(new Button.OnButtonEventListener() {
                @Override
                public void onButtonEvent(Button button, boolean pressed) {
                    Log.d(TAG, "button A pressed:" + pressed);
                }
            });
            buttonC.setOnButtonEventListener(new Button.OnButtonEventListener() {
                @Override
                public void onButtonEvent(Button button, boolean pressed) {
                    Log.d(TAG, "button A pressed:" + pressed);
                }
            });

            // Get native Android 'A' key events when button 'A' is pressed.
            ButtonInputDriver inputDriverA = RainbowHat.createButtonAInputDriver(
                    KeyEvent.KEYCODE_A      // keyCode to send
            );

            // Get native Android 'B' key events when button 'B' is pressed.
            ButtonInputDriver inputDriverB = RainbowHat.createButtonBInputDriver(
                    KeyEvent.KEYCODE_B      // keyCode to send
            );

            // Get native Android 'C' key events when button 'C' is pressed.
            ButtonInputDriver inputDriverC = RainbowHat.createButtonCInputDriver(
                    KeyEvent.KEYCODE_C      // keyCode to send
            );

            inputDriverA.register();
            inputDriverB.register();
            inputDriverC.register();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
