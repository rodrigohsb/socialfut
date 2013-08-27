package activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

public class SplashActivity extends SherlockActivity implements Runnable
{
    private static long SLEEP_TIME = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Removes title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        // Removes notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.layout_splash_screen);

        // Start timer and launch main activity
        Handler handler = new Handler();
        handler.postDelayed(this, SLEEP_TIME);
    }

    @Override
    public void run()
    {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
