package br.com.socialfut.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import br.com.socialfut.R;
import br.com.socialfut.adapter.GamesListAdapter;
import br.com.socialfut.database.GameDB;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MyFutureActivity extends SherlockActivity
{
    private GameDB gameDB;

    private Context ctx;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Próximos jogos");

        ctx = this;

        setContentView(R.layout.grid1);
        final GridView gridView = (GridView) findViewById(R.id.gridview);

        gameDB = new GameDB(this);
        List<Game> games = gameDB.getAllOldGames();

        if (games.isEmpty())
        {
            // Nenhuma partida foi disputada ainda.
            this.alert();
        }
        else
        {
            gridView.setAdapter(new GamesListAdapter(this, games));
        }

        gridView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                Game game = (Game) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ctx, GameDetailsActivity.class);
                intent.putExtra("game", game);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.menu_lista_de_jogadores_sortear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            Intent intent = new Intent(this, MainFragment.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void alert()
    {
        android.content.DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                alertDialog.dismiss();
                startActivity(new Intent(MyFutureActivity.this, MainFragment.class));
                finish();
            }
        };

        alertDialog = new AlertUtils(MyFutureActivity.this).getAlertDialog(Constants.WARNING,
                Constants.NO_NEW_GAMES, positiveButton, null);

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}
