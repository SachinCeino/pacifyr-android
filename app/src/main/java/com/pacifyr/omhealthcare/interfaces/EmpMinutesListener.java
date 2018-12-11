package com.pacifyr.omhealthcare.interfaces;

public interface EmpMinutesListener {

    void onMinutesLeft(long minutes);

    void expired();

    void tick(long minutes);
}
