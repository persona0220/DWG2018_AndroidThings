package com.google.androidthings.education.mtg;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import static com.google.androidthings.education.mtg.Led.ALL;
import static com.google.androidthings.education.mtg.Led.BLUE;
import static com.google.androidthings.education.mtg.Led.CYAN;
import static com.google.androidthings.education.mtg.Led.GREEN;
import static com.google.androidthings.education.mtg.Led.ORANGE;
import static com.google.androidthings.education.mtg.Led.RED;
import static com.google.androidthings.education.mtg.Led.VIOLET;
import static com.google.androidthings.education.mtg.Led.WHITE;
import static com.google.androidthings.education.mtg.Led.YELLOW;
import static com.google.androidthings.education.mtg.MusicPlayer.Note.C;
import static com.google.androidthings.education.mtg.MusicPlayer.Note.D;
import static com.google.androidthings.education.mtg.MusicPlayer.Note.E;
import static com.google.androidthings.education.mtg.MusicPlayer.Note.F;
import static com.google.androidthings.education.mtg.MusicPlayer.Note.G;

/**
 * My Device!
 */

public class MyDevice {
    private static final String TAG = MyDevice.class.getSimpleName();

    private Led light;
    private Display display;
    private MusicPlayer music;
    private String answer = " ";
    private int cut = 0;
    private boolean song = true;

    public MyDevice(Display display, MusicPlayer music, Led light) {
        this.display = display;
        this.music = music;
        this.light = light;
    }

    public static void pause(double pauseTimeSec) {
        try {
            Thread.sleep((long)(pauseTimeSec * 1000.0));
        } catch (InterruptedException e) {
            Log.e(TAG, "Failed to sleep", e);
        }
    }

    /** 여기서부터 시작 */
    void 게임시작(){
        song = true;
        Random random = new Random();
        display.clear();
        light.off(ALL);
        answer = " ";

        int [] Colors = {RED, GREEN, BLUE, VIOLET, ORANGE, CYAN};

        cut = (int)(random.nextInt(3));

        int [] LEDColors = {RED, GREEN, BLUE, VIOLET, ORANGE, CYAN, ORANGE};
        if(Colors[cut] == RED)
            LEDColors[cut] = VIOLET;
        else if(Colors[cut] == GREEN)
            LEDColors[cut] = ORANGE;
        else if(Colors[cut] == BLUE)
            LEDColors[cut] = CYAN;

        Collections.shuffle(Arrays.asList(LEDColors));

        for(int i=0; i<7; i++){
            light.on(i, LEDColors[i]);
        }
        pause(2);
        light.off(ALL);
    }

    boolean 기다리기(){
        //WAITING
        long limit = 300;
        display.show("03.00");
        for(int i = 0 ;; i++){
            limit -= 1;
            light.on(i%7, WHITE);
            light.off((i+6)%7);
            display.show('0'+Long.toString(limit/100)+'.'+Long.toString(limit%100));
            pause(0.01);

            if((""+cut).equals(answer)){
                return true;
            }
            if(limit == 0 || !answer.equals(" ")){
                Log.e("msg", ""+cut);
                Log.e("msg", answer);
                display.show("00.00");
                return false;
            }
            display.clear();
        }
    }

    void 실패(){
        light.setBrightness(9);
        light.on(ALL, RED);
        display.show("FAIL");
        pause(3.0);
    }

    void 펑(){
        light.setBrightness(9);
        light.on(ALL, RED);
        display.show("****");
        song = false;
        //music.play(MusicPlayer.Note.C);
        pause(3.0);
    }

    void 축하(){
        light.off(ALL);
        music.stop();
        song = false;
        display.show("----");
        pause(3.0);
    }

    void 노래노래노래() {

        MusicPlayer.Note [] note = {MusicPlayer.Note.G, MusicPlayer.Note.B};
        int noteNum = 2;

        for(int i=0; i<noteNum; i++){
            if(!song) break;

            music.play(note[i]);
            pause(0.1);
            //music.stop();
        }
        /*
        // 도레미파솔
        music.play(C);
        pause(1);
        music.stop();
        music.play(D);
        pause(1);
        music.stop();
        music.play(E);
        pause(1);
        music.stop();
        music.play(F);
        pause(1);
        music.stop();
        music.play(G);
        pause(1);
        music.stop();*/
    }

    // DEMO 코드
    void 데모() {
        light.on(0, RED);
        light.on(1, ORANGE);
        light.on(2, YELLOW);
        light.on(3, GREEN);
        light.on(4, CYAN);
        light.on(5, BLUE);
        light.on(6, VIOLET);
        pause(1);

        light.on(0, WHITE);
        light.on(1, RED);
        light.on(2, ORANGE);
        light.on(3, YELLOW);
        light.on(4, GREEN);
        light.on(5, CYAN);
        light.on(6, BLUE);
        pause(1);

        light.on(0, WHITE);
        light.on(1, WHITE);
        light.on(2, RED);
        light.on(3, ORANGE);
        light.on(4, YELLOW);
        light.on(5, GREEN);
        light.on(6, BLUE);
        pause(1);

        light.on(0, WHITE);
        light.on(1, WHITE);
        light.on(2, WHITE);
        light.on(3, RED);
        light.on(4, ORANGE);
        light.on(5, YELLOW);
        light.on(6, GREEN);
        pause(1);

        light.on(0, WHITE);
        light.on(1, WHITE);
        light.on(2, WHITE);
        light.on(3, WHITE);
        light.on(4, RED);
        light.on(5, ORANGE);
        light.on(6, YELLOW);
        pause(1);

        light.on(0, WHITE);
        light.on(1, WHITE);
        light.on(2, WHITE);
        light.on(3, WHITE);
        light.on(4, WHITE);
        light.on(5, RED);
        light.on(6, ORANGE);
        pause(1);

        light.on(0, WHITE);
        light.on(1, WHITE);
        light.on(2, WHITE);
        light.on(3, WHITE);
        light.on(4, WHITE);
        light.on(5, WHITE);
        light.on(6, RED);
        pause(1);

        light.on(ALL, WHITE);
        pause(1);

        display.show("NABI");
        pause(1);

        display.show("ABIY");
        pause(0.5);

        display.show("BIYA");
        pause(0.5);

        music.playAll(0.5,
                G, E, E, E, F, D, D, D, C, D, E, F, G, G, G, G,
                G, E, E, E, F, D, D, D, C, E, G, G, E, E, E, E);
    }

    void musicAndLight(MusicPlayer.Note note) {
        int diff = note.value - C.value;
        light.on(diff, WHITE);
        music.play(note);
        pause(1);
        light.off(diff);
        music.stop();
    }

    public void setAnswer(String str){
        this.answer = str;
    }

    public boolean getSong(){ return this.song; }
}
