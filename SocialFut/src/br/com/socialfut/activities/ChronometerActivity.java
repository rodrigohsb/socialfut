package br.com.socialfut.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import br.com.socialfut.R;
import br.com.socialfut.helper.Chronometer;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Constants;

import com.actionbarsherlock.app.SherlockActivity;

public class ChronometerActivity extends SherlockActivity
{

    Chronometer mChronometer;

    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cronometro);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Cronômetro");

        ctx = this;

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mChronometer.setMaxTime(30100);

        mChronometer.setOnLongClickListener(new OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
                dialog.setTitle("Selecione o tempo de jogo");

                LayoutInflater li = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View dialogView = li.inflate(R.layout.layout_choose_position, null);
                dialog.setView(dialogView);

                final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner1);

                dialog.setPositiveButton("Escolher", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        switch (spinner.getSelectedItemPosition())
                        {
                        case 0:
                            mChronometer.setMaxTime(30100);
                            break;
                        case 1:
                            mChronometer.setMaxTime(30100);
                            break;
                        case 2:
                            mChronometer.setMaxTime(30100);
                            break;
                        case 3:
                            mChronometer.setMaxTime(30100);
                            break;
                        case 4:
                            mChronometer.setMaxTime(30100);
                            break;
                        case 5:
                            mChronometer.setMaxTime(30100);
                            break;
                        }
                    }
                });
                dialog.setCancelable(true);
                dialog.show();

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, R.array.time_options,
                        android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                return true;
            }
        });

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
                    mChronometer.setLongClickable(false);
                    stopbutton.setClickable(true);
                }
            }
        });

        stopbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (stopbutton.getText().equals("Pausar"))
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
                mChronometer.setLongClickable(true);
                mChronometer.reset();
                stopbutton.setText("Pausar");
                stopbutton.setClickable(false);
            }
        });
    }

    @Override
    protected void onResume()
    {
        if (Constants.TIME > 0)
        {
            mChronometer.setBase(Constants.TIME);
        }
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Constants.TIME = mChronometer.getBase();
    }
}
