package br.com.socialfut.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import br.com.socialfut.R;
import br.com.socialfut.adapter.PlayerListAdapter;
import br.com.socialfut.drawer.EndlessListView;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;
import br.com.socialfut.webservices.GameREST;
import br.com.socialfut.webservices.WebServiceClient;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente proposito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 04/10/2013
 * 
 */
public class GameDetailsActivity extends SherlockActivity
{

    private SimpleDateFormat dateFormat;

    private ToggleButton toggleButton;

    private Context ctx;

    private Game game;

    private boolean mHaveMoreDataToLoad;

    private EndlessListView endlessListView;

    private PlayerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Detalhes da Partida");

        setContentView(R.layout.layout_game_details);

        ctx = this;

        Bundle bundle = getIntent().getExtras();
        game = (Game) bundle.getSerializable("game");

        dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN_FOR_USER);

        /** Titulo */
        TextView titleGameDetails = (TextView) findViewById(R.id.titleGameDetails);
        titleGameDetails.setText(game.getTitle());

        /** Criada em */
        TextView createdDateGameDetails = (TextView) findViewById(R.id.createdDateGameDetails);
        createdDateGameDetails.setText(dateFormat.format(game.getCreatedDate()));

        /** Dia a ser disputada */
        TextView dateGameDetails = (TextView) findViewById(R.id.dateGameDetails);
        dateGameDetails.setText(dateFormat.format(game.getStartDate()));

        dateFormat = new SimpleDateFormat(Constants.HOUR_PATTERN);

        /** Horario */
        TextView hourGameDetails = (TextView) findViewById(R.id.hourGameDetails);
        hourGameDetails.setText(dateFormat.format(game.getStartDate()) + " - "
                + dateFormat.format(game.getFinishDate()));

        /** Endereco */
        TextView addressGameDetails = (TextView) findViewById(R.id.addressGameDetails);
        addressGameDetails.setText(game.getAddress());

        RatingBar rating = (RatingBar) findViewById(R.id.ratingBarGameDetails);

        new GameREST(ctx, game.getId(),rating, false).execute();

        toggleButton = (ToggleButton) findViewById(R.id.toggleButtonGameDetails);

        if (bundle.getString("old") != null)
        {
            toggleButton.setVisibility(View.GONE);
        }

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            sendConfirmation();
                        }
                    };
                    DialogInterface.OnClickListener negativeButton = new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                        }
                    };

                    AlertDialog dialog = new AlertUtils(ctx).getAlertDialog(Constants.WARNING, "Tem certeza?",
                            positiveButton, negativeButton);

                    dialog.show();
                }
                else
                {
                    sendDesconfirmation();
                }
            }

        });

        mHaveMoreDataToLoad = true;
        endlessListView = (EndlessListView) findViewById(R.id.endless);
    }

    private void sendConfirmation()
    {
        ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setMessage("Confirmando presenca...");
        dialog.setCancelable(false);
        dialog.show();

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "confirmation" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + game.getId());

        dialog.dismiss();

        if (resposta[1] == "OK")
        {
            Toast.makeText(this, "Voce acabou de confirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Nao foi possivel confirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDesconfirmation()
    {
        ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setMessage("Desconfirmando presenca...");
        dialog.setCancelable(false);
        dialog.show();

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "desconfirmation" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + game.getId());

        dialog.dismiss();

        if (resposta[1] == "OK")
        {
            Toast.makeText(this, "Voce acabou de desconfirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Nao foi possivel desconfirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMoreData()
    {
        new LoadMore().execute((Void) null);
    }

    private EndlessListView.OnLoadMoreListener loadMoreListener = new EndlessListView.OnLoadMoreListener()
    {

        @Override
        public boolean onLoadMore()
        {
            if (mHaveMoreDataToLoad)
            {
                loadMoreData();
            }
            return mHaveMoreDataToLoad;
        }
    };

    private class LoadMore extends AsyncTask<Void, Void, List<Player>>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected List<Player> doInBackground(Void... params)
        {
            String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "playersByGame" + Constants.SLASH
                    + game.getId());

            List<Player> jogadores = new ArrayList<Player>();
            mHaveMoreDataToLoad = false;
            adapter = new PlayerListAdapter(ctx, jogadores);
            endlessListView.setAdapter(adapter);
            endlessListView.setOnLoadMoreListener(loadMoreListener);

            return jogadores;
        }

        @Override
        protected void onPostExecute(List<Player> result)
        {
            super.onPostExecute(result);

            adapter.addItems(result);
            endlessListView.loadMoreCompleat();
            mHaveMoreDataToLoad = false;
        }
    }
}
