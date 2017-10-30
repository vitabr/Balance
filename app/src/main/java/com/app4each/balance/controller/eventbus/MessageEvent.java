package com.app4each.balance.controller.eventbus;

import android.os.Bundle;

/**
 * Created by vito on 30/10/2017.
 */

public class MessageEvent {
    public int id;
    public Bundle data;

    public MessageEvent(int messageId){
        this.id = messageId;
        data = new Bundle();
    }

    public MessageEvent(int messageId, Bundle data){
        this.id = messageId;
        this.data = data;
    }

    public String get(String key) {
        if(data == null)
            return null;

        return data.getString(key);
    }
}
