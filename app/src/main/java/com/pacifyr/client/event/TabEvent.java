package com.pacifyr.client.event;

/**
 * Created by ceino on 7/4/17.
 */

public class TabEvent
{
    public boolean isSuccess=false;
    public int pos=0;

    public TabEvent(boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.pos = 0;
    }

    public TabEvent(boolean isSuccess,int pos) {
        this.isSuccess = isSuccess;
        this.pos = pos;
    }
}
