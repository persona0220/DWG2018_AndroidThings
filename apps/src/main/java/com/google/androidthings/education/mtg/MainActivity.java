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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManagerService;

import java.io.IOException;

import static com.google.androidthings.education.mtg.Led.BLUE;
import static com.google.androidthings.education.mtg.Led.GREEN;
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

        final int[] g_flag = {0,0}; // [0]==1:replay, [0]==2:quit //[1]=# of answers
        final ImageButton img_replay = (ImageButton)findViewById(R.id.img_replay);
        img_replay.setOnClickListener(new ImageButton.OnClickListener() {
            @Override public void onClick(View view) {
                if(g_flag[0] == 0) g_flag[0] = 1;
            }
        }) ;
        final ImageButton img_quit = (ImageButton)findViewById(R.id.img_quit);
        img_quit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override public void onClick(View view) {
                if(g_flag[0] == 0) g_flag[0] = 2;
            }
        }) ;

        출처: http://recipes4dev.tistory.com/55 [개발자를 위한 레시피]


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
                    while (true) myDevice.노래노래노래();
                }
            }.start();
            new Thread() {
                @Override
                public void run() {

                    light.on(0, BLUE);
                    light.on(3, GREEN);
                    light.on(6, RED);

                    display.show("BOMB");
                    myDevice.pause(5);

                    while(true) {
                        // initial screen
                        runOnUiThread(new Runnable() {
                            public void run() {
                                img_back.setImageResource(R.drawable.g_back);
                                img_sci.setVisibility(View.VISIBLE);
                                img_wire1.setVisibility(View.VISIBLE);
                                img_wire2.setVisibility(View.VISIBLE);
                                img_wire3.setVisibility(View.VISIBLE);
                                img_replay.setVisibility(View.INVISIBLE);
                                img_quit.setVisibility(View.INVISIBLE);
                            }
                        });

                        myDevice.게임시작();
                        boolean result = myDevice.기다리기();

                        if (result == false) {
                            //실패
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    img_back.setImageResource(R.drawable.g_boom);
                                    img_sci.setVisibility(View.INVISIBLE);
                                    img_wire1.setVisibility(View.INVISIBLE);
                                    img_wire2.setVisibility(View.INVISIBLE);
                                    img_wire3.setVisibility(View.INVISIBLE);
                                    img_replay.setVisibility(View.VISIBLE);
                                    img_quit.setVisibility(View.VISIBLE);
                                }
                            }); //change the screen
                            myDevice.펑();
                            while(true){
                                if(g_flag[0] != 0) break;
                            }
                            if(g_flag[0] == 1){
                                g_flag[0] = 0;
                                g_flag[1] = 0;
                                continue;
                            }
                            else if(g_flag[0] == 2){
                                break;
                            }
                        } else {
                            //해제 성공
                            g_flag[1] += 1;
                            if(g_flag[1] == 1){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        img_back.setImageResource(R.drawable.g_1);
                                        img_sci.setVisibility(View.INVISIBLE);
                                        img_wire1.setVisibility(View.INVISIBLE);
                                        img_wire2.setVisibility(View.INVISIBLE);
                                        img_wire3.setVisibility(View.INVISIBLE);
                                        img_replay.setVisibility(View.INVISIBLE);
                                        img_quit.setVisibility(View.INVISIBLE);
                                    }
                                }); //change the screen
                                myDevice.pause(2);
                                continue;
                            }
                            else if(g_flag[1] == 2){
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        img_back.setImageResource(R.drawable.g_2);
                                        img_sci.setVisibility(View.INVISIBLE);
                                        img_wire1.setVisibility(View.INVISIBLE);
                                        img_wire2.setVisibility(View.INVISIBLE);
                                        img_wire3.setVisibility(View.INVISIBLE);
                                        img_replay.setVisibility(View.INVISIBLE);
                                        img_quit.setVisibility(View.INVISIBLE);
                                    }
                                }); //change the screen
                                myDevice.pause(2);
                                continue;
                            }
                            else{
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        img_back.setImageResource(R.drawable.g_cong);
                                        img_sci.setVisibility(View.INVISIBLE);
                                        img_wire1.setVisibility(View.INVISIBLE);
                                        img_wire2.setVisibility(View.INVISIBLE);
                                        img_wire3.setVisibility(View.INVISIBLE);
                                        img_replay.setVisibility(View.VISIBLE);
                                        img_quit.setVisibility(View.VISIBLE);
                                    }
                                }); //change the screen
                                myDevice.축하();
                                while(true){
                                    if(g_flag[0] != 0) break;
                                }
                                if(g_flag[0] == 1){
                                    g_flag[0] = 0;
                                    g_flag[1] = 0;
                                    continue;
                                }
                                else if(g_flag[0] == 2){
                                    break;
                                }
                            }
                        }
                    }
                    if(g_flag[0] == 2 && g_flag[1] != 3){
                        g_flag[0] = 0;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                img_back.setImageResource(R.drawable.g_fail);
                                img_sci.setVisibility(View.INVISIBLE);
                                img_wire1.setVisibility(View.INVISIBLE);
                                img_wire2.setVisibility(View.INVISIBLE);
                                img_wire3.setVisibility(View.INVISIBLE);
                                img_replay.setVisibility(View.INVISIBLE);
                            }
                        }); //change the screen
                        myDevice.실패();
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
