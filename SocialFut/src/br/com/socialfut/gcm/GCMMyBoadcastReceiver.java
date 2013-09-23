package br.com.socialfut.gcm;

import android.content.Context;
import br.com.socialfut.util.Constants;

import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMMyBoadcastReceiver extends GCMBroadcastReceiver
{
    protected String getGCMIntentServiceClassName(Context contest)
    {
        return Constants.GCM_INTENT_SERVICE;
    }
}