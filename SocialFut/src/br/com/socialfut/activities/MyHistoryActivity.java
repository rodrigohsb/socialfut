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

public class MyHistoryActivity extends SherlockActivity
{
    private Context ctx;

    private AlertDialog alertDialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Meu Historico");

        ctx = this;

        setContentView(R.layout.grid1);
        final GridView gridView = (GridView) findViewById(R.id.gridview);

        // for (int i = 0; i < 16; i++)
        // {
        // Game g = new Game("Titulo " + i, "Rua XPTO", new Date(), new Date(),
        // new Date());
        // new GameDB(ctx).saveGame(g);
        // }

        List<Game> games = new GameDB(this).getOldGames();

        if (games.isEmpty())
        {
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
                intent.putExtra("old", "old");
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
                finish();
            }
        };

        alertDialog = new AlertUtils(MyHistoryActivity.this).getAlertDialog(Constants.WARNING, Constants.NO_OLD_GAMES,
                positiveButton, null);

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

}