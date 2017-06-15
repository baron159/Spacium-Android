package io.spoton.spacium;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the version one
 * Created by scottbaron on 6/14/17.
 */

public class KiolidaScope extends AppCompatActivity {

    private KSView view;

    @Override
    protected void onCreate(Bundle savedInst){
        super.onCreate(savedInst);
        this.view = new KSView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.view.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.view.pause();
    }

    public class KSView extends SurfaceView implements Runnable{

        Thread drawThread = null;
        SurfaceHolder surfaceHolder;
        boolean running = false;

        public KSView(Context context) {
            super(context);
            surfaceHolder = getHolder();
        }

        @Override
        public void run() {
            while(running){
                if (!surfaceHolder.getSurface().isValid()){
                    continue;
                }

                Canvas kv = surfaceHolder.lockCanvas();
                kv.drawARGB(255, 150, 150, 75);
                surfaceHolder.unlockCanvasAndPost(kv);
            }
        }

        public void pause() {
            this.running = false;
            while(true){
                try {
                    drawThread.join();
                }
                catch (InterruptedException err){
                    err.printStackTrace();
                }
                break;
            }
            drawThread = null;
        }

        public void resume() {
            this.running = true;
            drawThread = new Thread(this);
            drawThread.start();
        }

    }

}
