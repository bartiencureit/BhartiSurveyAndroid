package com.encureit.samtadoot.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.encureit.samtadoot.listeners.MessageListener;


public class MessageReceiver extends BroadcastReceiver {
    public MessageReceiver() {
    }

    private static MessageListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Bundle data = intent.getExtras();
            Object[] pdus = (Object[]) data.get("pdus"); //Protocol Data Unit

            for (Object o : pdus) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) o);
                String message = smsMessage.getMessageBody();
                Log.e("act_MR::", "otpMessage_: " + message);
                mListener.messageReceived(message);
            }
        } catch (Exception e) {
            Log.e("act_MR::", "Exception_: " + e);
            e.printStackTrace();
        }
    }

    public static void bindListener(MessageListener listener){
        mListener = listener;
    }
}
