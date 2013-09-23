package br.com.socialfut.activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import br.com.socialfut.R;
import br.com.socialfut.helper.Chronometer;

import com.actionbarsherlock.app.SherlockActivity;

public class ChronometerActivity extends SherlockActivity
{

    Chronometer mChronometer;

    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cronometro);

        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setMaxTime(30100);

        final Button startbutton = (Button) findViewById(R.id.start);
        final Button stopbutton = (Button) findViewById(R.id.pause);
        final Button resetbutton = (Button) findViewById(R.id.clear);

        startbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mChronometer.start();
                if (!stopbutton.isClickable())
                {
                    stopbutton.setClickable(true);
                }
            }
        });

        stopbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (stopbutton.getText() == "Pausar")
                {
                    mChronometer.pause();
                    stopbutton.setText("Continuar");
                }
                else
                {
                    stopbutton.setText("Pausar");
                    mChronometer.resume();
                }
            }
        });

        resetbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mChronometer.reset();
                stopbutton.setText("Pausar");
                stopbutton.setClickable(false);
            }
        });
    }
}
