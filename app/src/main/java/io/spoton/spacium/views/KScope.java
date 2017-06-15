package io.spoton.spacium.views;

import android.content.Context;
import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The first of the views for the application
 * Created by scottbaron on 6/15/17.
 */

public class KScope extends SurfaceView implements Runnable, SpacView {

    private Context context;
    private AtomicLong rotaionRate;
    private Thread drawThread;

    public KScope(Context context){
        super(context);
        this.context = context;
        this.rotaionRate = new AtomicLong();
    }

    @Override
    public void activateView() {

    }

    @Override
    public void pauseView() {

    }

    @Override
    public void setRotaion(float rotRate) {
        this.rotaionRate.set((long) rotRate);
    }

    @Override
    public void run() {

    }
}
