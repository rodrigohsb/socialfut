package br.com.socialfut.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import br.com.socialfut.R;
import br.com.socialfut.util.Constants;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import com.google.android.gcm.GCMRegistrar;

public class SplashActivity extends SherlockActivity implements Runnable
{
    private static long SLEEP_TIME = 2000;

    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ctx = this;

        /** Removes title bar */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /** Removes notification bar */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_splash_screen);

        /** salva deviceRegistrationId */
        GCM gcm = new GCM();
        gcm.execute();

        /** Start timer and launch main activity */
        Handler handler = new Handler();
        handler.postDelayed(this, SLEEP_TIME);
    }

    @Override
    public void run()
    {
        startActivity(new Intent(this, DrawerLayoutActivity.class));
        finish();
    }

    @Override
    protected void onDestroy()
    {
        GCMRegistrar.onDestroy(getApplicationContext());
        super.onDestroy();
    }

    private class GCM extends AsyncTask<Void, Void, Void>
    {

        public GCM()
        {
            super();
        }

        @Override
        protected Void doInBackground(Void... v)
        {
            register();
            return null;
        }

        private void register()
        {
            if (GCMRegistrar.isRegistered(ctx))
            {
                String regId = GCMRegistrar.getRegistrationId(ctx);
                Constants.DEVICE_REGISTRATION_ID = regId;
            }
            else
            {
                GCMRegistrar.checkDevice(ctx);
                GCMRegistrar.checkManifest(ctx);
                GCMRegistrar.register(ctx, Constants.PROJECT_NUMBER);
            }
        }
    }
}
