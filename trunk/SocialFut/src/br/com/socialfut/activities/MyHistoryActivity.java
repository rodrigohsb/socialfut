package br.com.socialfut.activities;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import br.com.socialfut.R;
import br.com.socialfut.adapter.GamesListAdapter;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;
import br.com.socialfut.webservices.WebServiceClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MyHistoryActivity extends SherlockActivity
{
    private Context ctx;

    private AlertDialog alertDialog;

    private GridView gridView;

    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Meu Historico");

        ctx = this;

        setContentView(R.layout.grid1);
        gridView = (GridView) findViewById(R.id.gridview);

        new GameREST().execute();

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

    private class GameREST extends AsyncTask<Void, Void, List<Game>>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(ctx);
            dialog.setMessage("Por favor, aguarde...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected List<Game> doInBackground(Void... params)
        {
            String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "oldGames" + Constants.SLASH
                    + Constants.USER_ID);
            return this.getGames(resposta[1]);
        }

        @Override
        protected void onPostExecute(List<Game> games)
        {
            super.onPostExecute(games);
            dialog.dismiss();
            if (games.isEmpty())
            {
                alert();
            }
            else
            {
                gridView.setAdapter(new GamesListAdapter(ctx, games));
            }
        }

        private List<Game> getGames(String text)
        {
            Gson g = new Gson();
            Type collectionType = new TypeToken<Collection<Game>>()
            {
            }.getType();
            Collection<Game> games = g.fromJson(text, collectionType);

            return (List<Game>) games;
        }
    }
}