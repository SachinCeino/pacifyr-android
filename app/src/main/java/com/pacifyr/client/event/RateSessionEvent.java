package com.pacifyr.client.event;

/**
 * Created by developer on 1/18/2018.
 */

public class RateSessionEvent {

	public String mSessionId="";
	public String mMessage="";

	public String getmSessionId() {
		return mSessionId;
	}

	public void setmSessionId(String mSessionId) {
		this.mSessionId = mSessionId;
	}

	public String getmMessage() {
		return mMessage;
	}

	public void setmMessage(String mMessage) {
		this.mMessage = mMessage;
	}

	public RateSessionEvent(String mSessionId, String mMessage) {
		this.mSessionId = mSessionId;
		this.mMessage = mMessage;
	}
}
