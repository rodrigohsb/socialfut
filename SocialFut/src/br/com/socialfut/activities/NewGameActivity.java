package br.com.socialfut.activities;

import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import br.com.socialfut.R;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;
import br.com.socialfut.webservices.WebServiceClient;

import com.actionbarsherlock.app.SherlockActivity;

public class NewGameActivity extends SherlockActivity
{

    private Context ctx;

    private TextView date;

    private TextView startHour;

    private TextView finishHour;

    private TextView address;

    private TextView title;

    private int startHourOfDay;

    private int startMinute;

    private int finishHourOfDay;

    private int finishMinute;

    private int year;

    private int month;

    private int day;

    private Calendar c;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_game);

        ctx = this;

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Nova Partida");

        date = (TextView) findViewById(R.id.dateNewGame);
        startHour = (TextView) findViewById(R.id.startHourNewGame);
        finishHour = (TextView) findViewById(R.id.finishHourNewGame);
        address = (TextView) findViewById(R.id.addressTxt);
        title = (TextView) findViewById(R.id.titleTxt);

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
                new DatePickerDialog(ctx, datePickerListener, year, month, day).show();
            }
        });

        /** Inicio */
        findViewById(R.id.btnChangeStartHourNewGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new TimePickerDialog(ctx, startHourPickerListener, startHourOfDay, startMinute, true).show();
            }
        });

        /** Termino */
        findViewById(R.id.btnChangeFinishHourNewGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new TimePickerDialog(ctx, finishHourPickerListener, finishHourOfDay, finishMinute, true).show();
            }
        });

        /** Criar */
        findViewById(R.id.btncreateNewGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (validate())
                {
                    c.set(year, month + 1, day, startHourOfDay, startMinute);
                    Calendar c1 = Calendar.getInstance();
                    c1.set(year, month + 1, day, finishHourOfDay, finishMinute);
                    new GameRest(title.getText().toString(), address.getText().toString(), c.getTimeInMillis(), c1
                            .getTimeInMillis()).execute();
                }
            }
        });
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

    private TimePickerDialog.OnTimeSetListener startHourPickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {

            startHourOfDay = hourOfDay;
            startMinute = minute;

            if (hourOfDay < 10 && minute < 10)
            {
                startHour.setText(new StringBuilder().append("0").append(startHourOfDay).append(" : ").append("0")
                        .append(startMinute));
            }
            else if (minute < 10)
            {
                startHour.setText(new StringBuilder().append(startHourOfDay).append(" : ").append("0")
                        .append(startMinute));
            }
            else if (hourOfDay < 10)
            {
                startHour.setText(new StringBuilder().append("0").append(startHourOfDay).append(" : ")
                        .append(startMinute));
            }
            else
            {
                startHour.setText(new StringBuilder().append(startHourOfDay).append(" : ").append(startMinute));
            }
        }
    };

    private TimePickerDialog.OnTimeSetListener finishHourPickerListener = new TimePickerDialog.OnTimeSetListener()
    {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
        {

            finishHourOfDay = hourOfDay;
            finishMinute = minute;

            if (hourOfDay < 10 && minute < 10)
            {
                finishHour.setText(new StringBuilder().append("0").append(finishHourOfDay).append(" : ").append("0")
                        .append(finishMinute));
            }
            else if (minute < 10)
            {
                finishHour.setText(new StringBuilder().append(finishHourOfDay).append(" : ").append("0")
                        .append(finishMinute));
            }
            else if (hourOfDay < 10)
            {
                finishHour.setText(new StringBuilder().append("0").append(finishHourOfDay).append(" : ")
                        .append(finishMinute));
            }
            else
            {
                finishHour.setText(new StringBuilder().append(finishHourOfDay).append(" : ").append(finishMinute));
            }
        }
    };

    private boolean validate()
    {
        if (date.getText().toString().trim().equals("") || title.getText().toString().trim().equals("")
                || address.getText().toString().trim().equals("") || startHour.getText().toString().equals(""))
        {
            showWarning("Por favor, preencha os dados da partida!", null, null);
            return false;
        }
        if (c.before(Calendar.getInstance()))
        {
            showWarning("Esse dia ja passou!", null, null);
            return false;
        }
        if (finishHourOfDay < startHourOfDay || (finishHourOfDay == startHourOfDay && finishMinute <= startMinute))
        {
            /** Termino nao pode ser antes do inicio */
            showWarning(Constants.END_BEFORE_START, null, null);
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, DrawerLayoutActivity.class));
    }

    private void showWarning(String text, final Class<?> cls, final Long gameId)
    {
        android.content.DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                if (cls != null)
                {
                    Intent it = new Intent(ctx, cls);
                    if (gameId != null)
                    {
                        it.putExtra("gameId", gameId);
                    }
                    startActivity(it);
                    finish();
                }
                alertDialog.dismiss();
            }
        };

        alertDialog = new AlertUtils(ctx).getAlertDialog(Constants.WARNING, text, positiveButton, null);

        alertDialog.show();
    }

    /**
     * 
     * AsyncTask p/ criar partida.
     * 
     * @author Rodrigo
     * 
     */
    private class GameRest extends AsyncTask<Void, String[], String[]>
    {

        private ProgressDialog dialog;

        private String title;

        private String address;

        private long startDate;

        private long finishDate;

        public GameRest(String title, String address, long startDate, long finishDate)
        {
            super();
            this.title = title;
            this.address = address;
            this.startDate = startDate;
            this.finishDate = finishDate;
        }

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage("Por favor, aguarde...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(Void... params)
        {
            StringBuilder sb = new StringBuilder(Constants.URL_GAME_WS).append("createGame").append(Constants.SLASH)
                    .append(startDate).append(Constants.SLASH).append(finishDate);
            String[] resposta = WebServiceClient.put(sb.toString(), title, address);
            return resposta;
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            dialog.dismiss();
            super.onPostExecute(result);
            if ("NOK".equals(result[1]))
            {
                showWarning("Por favor, tente mais tarde!", DrawerLayoutActivity.class, null);
            }
            else
            {
                showWarning("Partida criada! Escale seus amigos!", PlayerListActivity.class,
                        Long.valueOf(result[1]));
            }
        }
    }
}
