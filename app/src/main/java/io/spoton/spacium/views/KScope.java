package io.spoton.spacium.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicLong;

import io.spoton.spacium.R;

/**
 * The first of the views for the application
 * Created by scottbaron on 6/15/17.
 */

public class KScope extends SurfaceView implements Runnable, SpacView {

    private Context context;
    private AtomicLong rotationRate;
    private Thread drawThread;
    private boolean running;
    private SurfaceHolder surfaceHolder;
    private Resources res;

    int screenHeight;
    int screenWidth;
    float rotationDegree = 0;
// Yellow 255, 247, 22
    private double rValue = 255;
    private double gValue = 0;
    private double bValue = 0;

    private int colorSwitch = 0;

    private VectorDrawableCompat cog;
    private VectorDrawableCompat particalsInner;
    private VectorDrawableCompat particalsOutter;
    private VectorDrawableCompat spikeBall;

    public KScope(Context context, DisplayMetrics metrics){
        super(context);
        this.context = context;
        this.rotationRate = new AtomicLong();
        this.running = false;
        this.surfaceHolder = getHolder();
        this.res = context.getResources();
        this.screenHeight = metrics.heightPixels;
        this.screenWidth = metrics.widthPixels;

        this.cog = VectorDrawableCompat.create(this.res, R.drawable.cog, null);
        this.particalsInner = VectorDrawableCompat.create(this.res, R.drawable.particals1, null);
        this.particalsOutter = VectorDrawableCompat.create(this.res, R.drawable.particals1, null);
        this.spikeBall = VectorDrawableCompat.create(this.res, R.drawable.spikeball, null);
    }

    @Override
    public void activateView() {
        this.running = true;

        this.cog.setBounds(
                (-((this.screenHeight/2)-(this.screenWidth/2))),
                0,
                ((this.screenHeight/2)+(this.screenWidth/2)),
                this.screenHeight
        );

        this.spikeBall.setBounds(
                (-((this.screenHeight/2)-(this.screenWidth/2)))+300,
                300,
                ((this.screenHeight/2)+(this.screenWidth/2))-300,
                this.screenHeight-300
        );

        this.particalsInner.setBounds(
                (-((this.screenHeight/2)-(this.screenWidth/2)))+600,
                600,
                ((this.screenHeight/2)+(this.screenWidth/2))-600,
                this.screenHeight-600
        );

        this.particalsOutter.setBounds(
                (-((this.screenHeight/2)-(this.screenWidth/2)))+150,
                150,
                ((this.screenHeight/2)+(this.screenWidth/2))-150,
                this.screenHeight-150
        );

        this.drawThread = new Thread(this);
        drawThread.start();
    }

    @Override
    public void pauseView() {
        this.running = false;
        try{
            drawThread.join();
        }
        catch (InterruptedException err){
            err.printStackTrace();
        }
        finally {
            drawThread = null;
        }
    }

    @Override
    public void setRotation(float rotRate) {
        this.rotationRate.set((long)rotRate*3);
    }

    @Override
    public void run() {
        while (this.running){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }
            this.rotationDegree += this.rotationRate.floatValue();

            Canvas canvas = surfaceHolder.lockCanvas();

            Paint paint = new Paint();

            switch (this.colorSwitch){
                case 0:
                    this.rValue = 255;
                    this.gValue += this.rotationRate.doubleValue();
                    if(this.gValue >= 255){
                        this.gValue = 255;
                        this.colorSwitch++;
                    }
                    break;
                case 1:
                    this.gValue = 255;
                    this.rValue -= this.rotationRate.doubleValue();
                    if(this.rValue <= 0){
                        this.rValue = 0;
                        this.colorSwitch++;
                    }
                    break;
                case 2:
                    this.gValue = 255;
                    this.bValue += this.rotationRate.doubleValue();
                    if(this.bValue >= 255){
                        this.bValue = 255;
                        this.colorSwitch++;
                    }
                    break;
                case 3:
                    this.bValue =255;
                    this.gValue -= this.rotationRate.doubleValue();
                    if(this.gValue <= 0){
                        this.gValue = 0;
                        this.colorSwitch++;
                    }
                    break;
                case 4:
                    this.bValue = 255;
                    this.rValue += this.rotationRate.doubleValue();
                    if(this.rValue >= 255){
                        this.rValue = 255;
                        this.colorSwitch++;
                    }
                    break;
                case 5:
                    this.rValue = 255;
                    this.bValue -= this.rotationRate.doubleValue();
                    if(this.bValue <= 0){
                        this.bValue = 0;
                        this.colorSwitch = 0;
                    }
                    break;
            }
            Log.i("RGB Tag",
                    "R: " + Integer.toString((int) this.rValue) +
                    " G: " + Integer.toString((int)this.gValue) +
                    " B: " + Integer.toString((int)this.bValue)
            );


// Drawing the Background
            canvas.drawARGB(255, ((int) this.rValue), ((int)this.gValue), ((int)this.bValue));
// Outer
            paint.setARGB(
                    255,
                    Math.abs(255-((int)this.rValue*2)),
                    Math.abs(255-((int)this.gValue*2)),
                    Math.abs(255-((int)this.bValue*2))
            );
            this.particalsOutter.setColorFilter(paint.getColor(), PorterDuff.Mode.SRC_IN);
            this.particalsOutter.draw(canvas);


// First Rotation for the cog
            canvas.rotate(
                    this.rotationDegree,
                    (int)this.screenWidth/2,
                    (int)this.screenHeight/2
            );

// Cog Logic
            paint.setARGB(
                    255,
                    Math.abs(80-(int)this.rValue),
                    Math.abs(40-(int)this.gValue),
                    Math.abs(10-(int)this.bValue)
            );
            this.cog.setColorFilter(paint.getColor(), PorterDuff.Mode.SRC_IN);
            this.cog.draw(canvas);
//Inner Parts
            paint.setARGB(
                    255,
                    Math.abs(255-(int)this.rValue),
                    Math.abs(255-(int)this.gValue),
                    Math.abs(255-(int)this.bValue)
            );
            this.particalsInner.setColorFilter(paint.getColor(), PorterDuff.Mode.SRC_IN);
            this.particalsInner.draw(canvas);

// Canvas Rotation
            canvas.rotate(
                    (-(this.rotationDegree*2)),
                    (int)this.screenWidth/2,
                    (int)this.screenHeight/2
            );
// SpikeBall Logic
            paint.setARGB(
                    255,
                    Math.abs(255-(int)this.rValue),
                    Math.abs(255-(int)this.gValue),
                    Math.abs(255-(int)this.bValue)
            );
            this.spikeBall.setColorFilter(paint.getColor(), PorterDuff.Mode.SRC_IN);
            this.spikeBall.draw(canvas);


            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

}
