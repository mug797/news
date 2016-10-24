package com.example.mkhade.newyorktimessearch.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.example.mkhade.newyorktimessearch.R;
import com.example.mkhade.newyorktimessearch.utils.NetworkUtil;

/**
 * Created by mkhade on 10/21/2016.
 */

public class NetworkChangeReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(context);
        Log.e("DEBUG", "status" + status);
        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
                View parentlayout = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
                Snackbar.make(parentlayout, R.string.snackbar_text_internet_connection, Snackbar.LENGTH_LONG)
                        //.setAction(R.string.snackbar_action, myOnClickListener)  // action text on the right side
                        //.setActionTextColor(R.color.green)
                        .setDuration(3000).show();
            } else {
                /*Snackbar.make(parentView, R.string.snackbar_text, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action, myOnClickListener)  // action text on the right side
                        .setActionTextColor(R.color.green)
                        .setDuration(3000).show();*/
            }
        }
    }
}