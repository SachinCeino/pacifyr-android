package com.pacifyr.client.event;

/**

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
