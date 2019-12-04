package com.pacifyr.client.event;

/**
 * Created by Mani-Ceino on 1/17/2018.
 */

public class CallTimerEvent {
	//public Boolean isSuccess=false;
	public Boolean isFinishCall=false;
	public CallTimerEvent() {
		//this.isSuccess = true;
		isFinishCall=false;
	}
	public CallTimerEvent(Boolean isFinishCall) {
		//this.isSuccess = true;
		this.isFinishCall=isFinishCall;
	}
}
