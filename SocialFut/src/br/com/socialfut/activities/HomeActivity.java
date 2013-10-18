package br.com.socialfut.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.socialfut.R;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class HomeActivity extends SherlockFragmentActivity implements OnClickListener
{

    private AlertDialog dialog;

    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        context = this;
        setContentView(R.layout.layout_inicio);
        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        final Button players = (Button) findViewById(R.id.playerList);
        players.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.menu_inicio, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {

        case R.id.dice:
            startActivity(new Intent(this, PlayerListForSort.class));
            break;

        case R.id.cronometro:
            startActivity(new Intent(this, ChronometerActivity.class));
            break;

        case R.id.subtitle:
            startActivity(new Intent(this, LegendaActivity.class));
            break;

        case R.id.exit:
            close();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void close()
    {
        DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                finish();
            }
        };
        DialogInterface.OnClickListener negativeButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            }
        };

        dialog = new AlertUtils(context).getAlertDialog(Constants.WARNING, Constants.CLOSE_APP, positiveButton,
                negativeButton);

        dialog.show();

    }

    @Override
    public void onClick(View v)
    {

        if (v.getId() == R.id.playerList)
        {
            startActivity(new Intent(this, PlayerListActivity.class));
        }
    }
}
