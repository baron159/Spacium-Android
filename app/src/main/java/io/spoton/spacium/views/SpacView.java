package io.spoton.spacium.views;

import android.view.SurfaceView;

import java.util.concurrent.atomic.AtomicLong;

/**
 * This is the generalized View
 * Created by scottbaron on 6/15/17.
 */

public interface SpacView {
    void activateView();
    void pauseView();
    void setRotation(float rotRate);

}
