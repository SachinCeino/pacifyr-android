package com.pacifyr.client.interfaces;

public interface EmpMinutesListener {

    void onMinutesLeft(long minutes);

    void expired();

    void tick(long minutes);
}
