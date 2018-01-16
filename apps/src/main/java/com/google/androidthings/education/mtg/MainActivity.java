/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.androidthings.education.mtg;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import static com.google.androidthings.education.mtg.Led.ALL;
import static com.google.androidthings.education.mtg.Led.RED;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Led light;
    private Display display;
    private MusicPlayer music;
    private MyDevice myDevice;

    private Gpio mButtonGpioA;
    private Gpio mButtonGpioB;
    private Gpio mButtonGpioC;

    public MainActivity() throws IOException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView img_back = (ImageView)findViewById(R.id.img_back);
        final ImageView img_sci = (ImageView)findViewById(R.id.img_sci);
        final ImageView img_wire1 = (ImageView)findViewById(R.id.img_wire1);
        final ImageView img_wire2 = (ImageView)findViewById(R.id.img_wire2);
        final ImageView img_wire3 = (ImageView)findViewById(R.id.img_wire3);


        Log.d(TAG, "onCreate");

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            mButtonGpioA = service.openGpio("GPIO6_IO14");
            mButtonGpioA.setDirection(Gpio.DIRECTION_IN);
            mButtonGpioA.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mButtonGpioA.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    myDevice.setAnswer("0");
                    Log.i(TAG, "GPIO changed, buttonA pressed");
                    // Return true to continue listening to events
                    return true;
                }
            });
            mButtonGpioB = service.openGpio("GPIO6_IO15");
            mButtonGpioB.setDirection(Gpio.DIRECTION_IN);
            mButtonGpioB.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mButtonGpioB.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    myDevice.setAnswer("1");
                    Log.i(TAG, "GPIO changed, buttonB pressed");
                    // Return true to continue listening to events
                    return true;
                }
            });
            mButtonGpioC = service.openGpio("GPIO2_IO07");
            mButtonGpioC.setDirection(Gpio.DIRECTION_IN);
            mButtonGpioC.setEdgeTriggerType(Gpio.EDGE_FALLING);
            mButtonGpioC.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    myDevice.setAnswer("2");
                    Log.i(TAG, "GPIO changed, buttonC pressed");
                    // Return true to continue listening to events
                    return true;
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }

        display = new Display();
        music = new MusicPlayer();
        light = new Led();
        myDevice = new MyDevice(display, music, light);

        if (light.open() && display.open() && music.open()) {
            new Thread() {

                @Override
                public void run() {
                    myDevice.pause(1);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            img_back.setImageResource(R.drawable.g_back);
                            img_sci.setVisibility(View.VISIBLE);
                            img_wire1.setVisibility(View.VISIBLE);
                            img_wire2.setVisibility(View.VISIBLE);
                            img_wire3.setVisibility(View.VISIBLE);
                        }
                    });
                    myDevice.pause(1);
                    myDevice.게임시작();

                    boolean result = myDevice.기다리기();

                    if(result == false) {
                        //실패
                        runOnUiThread(new Runnable() {
                            public void run() {
                                img_back.setImageResource(R.drawable.g_boom);
                                img_sci.setVisibility(View.INVISIBLE);
                                img_wire1.setVisibility(View.INVISIBLE);
                                img_wire2.setVisibility(View.INVISIBLE);
                                img_wire3.setVisibility(View.INVISIBLE);
                            }
                        }); //change the screen
                        myDevice.펑();
                    }
                    else{
                        //해제 성공
                        myDevice.축하();
                    }
                    finish();
                }
            }.start();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        light.close();
        display.close();
        music.close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_A) {
            myDevice.setAnswer("A");
            Log.e("BUTTON", "A pressed");
        }

        if (keyCode == KeyEvent.KEYCODE_B) {
            myDevice.setAnswer("B");
            Log.e("BUTTON", "B pressed");
        }

        if (keyCode == KeyEvent.KEYCODE_C) {
            myDevice.setAnswer("C");
            Log.e("BUTTON", "C pressed");
        }
        return super.onKeyDown(keyCode, event);
    }
}