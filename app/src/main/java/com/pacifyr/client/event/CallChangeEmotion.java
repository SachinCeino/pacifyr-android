package com.pacifyr.client.event;

import com.pacifyrapp.utilitylibrary.model.pacifyr.MEmotion;

/**
 * Created by ceino on 7/4/17.
 */

public class CallChangeEmotion {
    private MEmotion emotion;

    public CallChangeEmotion(MEmotion emotion) {
        this.emotion = emotion;
    }

    public MEmotion getEmotion() {
        return emotion;
    }

    public void setEmotion(MEmotion emotion) {
        this.emotion = emotion;
    }
}
