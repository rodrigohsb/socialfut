package br.com.socialfut.activities;

import java.text.DateFormat;
import java.util.Calendar;

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
import android.widget.Toast;
import br.com.socialfut.R;
import br.com.socialfut.util.ActionBar;

import com.actionbarsherlock.app.SherlockActivity;

public class NewGameActivity extends SherlockActivity
{

    private Context ctx;

    private TextView date;

    private TextView hour;

    private TextView address;

    private int hourOfDay;

    private int minute;

    private int year;

    private int month;

    private int day;

    private Calendar c;

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
        address = (TextView) findViewById(R.id.addressTxt);

        c = Calendar.getInstance();
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
                if (date.getText().toString() != "" && hour.getText().toString() != "" && address.getText().toString() != "" )
                {
                    // TODO Pegar informacoes e passar ao WebServices p/ criar jogo.
                    c.set(year, month + 1, day, hourOfDay, minute);
                }
                else
                {
                    Toast.makeText(ctx, "Por favor, preencha os dados da partida!", Toast.LENGTH_SHORT).show();
                }
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
            DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
            c.set(year, month, day);
            date.setText(df.format(c.getTime()));
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