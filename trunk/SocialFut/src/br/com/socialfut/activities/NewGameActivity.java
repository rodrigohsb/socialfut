package br.com.socialfut.activities;

import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.socialfut.R;
import br.com.socialfut.database.GameDB;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.webservices.GameREST;

import com.actionbarsherlock.app.SherlockActivity;

public class NewGameActivity extends SherlockActivity
{

    private Context ctx;

    private TextView date;

    private TextView hour;

    private int hourOfDay;

    private int minute;

    private int year;

    private int month;

    private int day;

    static final int DATE_DIALOG_ID = 999;

    static final int HOUR_DIALOG_ID = 888;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_game);

        ctx = this;

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Nova Partida");

        date = (TextView) findViewById(R.id.dateNewGame);

        hour = (TextView) findViewById(R.id.hourNewGame);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        /** Data */
        findViewById(R.id.btnChangeDateNewGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(DATE_DIALOG_ID);
            }
        });

        /** Hora */
        findViewById(R.id.btnChangeHourNewGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog(HOUR_DIALOG_ID);
            }
        });

        /** Criar */
        findViewById(R.id.btncreateNewGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                new GameREST(ctx, title, address, startDate, finishDate, true);
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id)
        {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        case HOUR_DIALOG_ID:
            return new TimePickerDialog(this, hourPickerListener, hourOfDay, minute, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener()
    {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay)
        {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            date.setText(new StringBuilder().append(day).append(" / ").append(month + 1).append(" / ").append(year));
        }
    };

    private TimePickerDialog.OnTimeSetListener hourPickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {

            NewGameActivity.this.hourOfDay = hourOfDay;
            NewGameActivity.this.minute = minute;

            if (hourOfDay < 10 && minute < 10)
            {
                hour.setText(new StringBuilder().append("0").append(NewGameActivity.this.hourOfDay).append(" : ")
                        .append("0").append(NewGameActivity.this.minute));
            }
            else if (minute < 10)
            {
                hour.setText(new StringBuilder().append(NewGameActivity.this.hourOfDay).append(" : ").append("0")
                        .append(NewGameActivity.this.minute));
            }
            else if (hourOfDay < 10)
            {
                hour.setText(new StringBuilder().append("0").append(NewGameActivity.this.hourOfDay).append(" : ")
                        .append(NewGameActivity.this.minute));
            }
            else
            {
                hour.setText(new StringBuilder().append(NewGameActivity.this.hourOfDay).append(" : ")
                        .append(NewGameActivity.this.minute));
            }
        }
    };

}