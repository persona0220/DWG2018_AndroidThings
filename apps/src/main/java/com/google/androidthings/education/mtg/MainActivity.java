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


import static com.google.androidthings.education.mtg.Led.RED;
import static com.google.androidthings.education.mtg.MusicPlayer.Note;
import static com.google.androidthings.education.mtg.Led.ALL;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Led light;
    private Display display;
    private MusicPlayer music;
    private MyDevice myDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        display = new Display();
        music = new MusicPlayer();
        light = new Led();
        myDevice = new MyDevice(display, music, light);
        if (light.open() && display.open() && music.open()) {
            new Thread() {
                @Override
                public void run() {

                    //화면전환
                    myDevice.pause(1);
                    myDevice.게임시작();

                    //화면전환
                    myDevice.기다리기();
                    펑();

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
            Log.e("BUTTON", "A pressed");
        }

        if (keyCode == KeyEvent.KEYCODE_B) {
            Log.e("BUTTON", "B pressed");
        }

        if (keyCode == KeyEvent.KEYCODE_C) {
            Log.e("BUTTON", "C pressed");
        }
        return super.onKeyDown(keyCode, event);
    }

    void 펑(){
        light.setBrightness(9);
        light.on(ALL, RED);
        display.show("****");
        myDevice.pause(3.0);
    }
}
