package com.pacifyr.omhealthcare.model.interfaces;

import com.ceino.utilitylibrary.model.pacifyr.MUser;
import com.ceino.utilitylibrary.model.pacifyr.ModelUserProfile;
import com.pacifyr.omhealthcare.event.CallEvent;
import com.pacifyr.omhealthcare.event.ChatEvent;

/**
 * Created by Mani-Ceino on 2/9/2018.
 */

public interface   CallChatEventListner {


	public abstract void onCallEventClick(final CallEvent event);
	public abstract void onChatEventClick(final ChatEvent event);

	public abstract void onException(Exception e);
	public abstract void onError(String e);
}
