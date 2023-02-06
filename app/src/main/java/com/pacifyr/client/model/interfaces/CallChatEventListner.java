package com.pacifyr.client.model.interfaces;

import com.pacifyr.client.event.CallEvent;
import com.pacifyr.client.event.ChatEvent;

/**

 */

public interface   CallChatEventListner {


	public abstract void onCallEventClick(final CallEvent event);
	public abstract void onChatEventClick(final ChatEvent event);

	public abstract void onException(Exception e);
	public abstract void onError(String e);
}
