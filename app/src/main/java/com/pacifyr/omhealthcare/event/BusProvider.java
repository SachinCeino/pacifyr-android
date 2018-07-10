package com.pacifyr.omhealthcare.event;


import com.squareup.otto.Bus;

/**
 * Created by ceino on 6/1/15.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {

        return BUS;
    }

    private BusProvider() {
    }
}

    /*private final Handler mainThread = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        }else {
            mainThread.post(new Runnable() {
                @Override
                public void run() {
                    post(event);
                }
            });
        }
    }
    public void nativePost(final Object event){
        super.post(event);
    }*/
