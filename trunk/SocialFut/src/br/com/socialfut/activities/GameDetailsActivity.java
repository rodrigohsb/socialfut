package br.com.socialfut.activities;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

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

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Detalhes da Partida");

        setContentView(R.layout.layout_game_details);

        ctx = this;

        Bundle bundle = getIntent().getExtras();
        game = (Game) bundle.getSerializable("game");

        /** Titulo */
        TextView titleGameDetails = (TextView) findViewById(R.id.titleGameDetails);
        titleGameDetails.setText(game.getTitle());

        /** Criada em */
        TextView createdDateGameDetails = (TextView) findViewById(R.id.createdDateGameDetails);
        createdDateGameDetails.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(game.getCreatedDate()));

        /** Dia a ser disputada */
        TextView dateGameDetails = (TextView) findViewById(R.id.dateGameDetails);
        dateGameDetails.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(game.getStartDate()));

        dateFormat = new SimpleDateFormat(Constants.HOUR_PATTERN);

        /** Horario */
        TextView hourGameDetails = (TextView) findViewById(R.id.hourGameDetails);
        hourGameDetails.setText(dateFormat.format(game.getStartDate()) + " - "
                + dateFormat.format(game.getFinishDate()));

        /** Endereco */
        TextView addressGameDetails = (TextView) findViewById(R.id.addressGameDetails);
        addressGameDetails.setText(game.getAddress());

        RatingBar rating = (RatingBar) findViewById(R.id.ratingBarGameDetails);

        new GameREST(ctx, game.getId(), rating).execute();

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
                    /** CONFIRMAR */
                    new TellToFriends(game.getId(), 0).execute();
                }
                else
                {
                    /** DESCONFIRMAR */
                    DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            new TellToFriends(game.getId(), 1).execute();
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
            }

        });

        mHaveMoreDataToLoad = true;
        endlessListView = (EndlessListView) findViewById(R.id.endless);
        endlessListView.setOnLoadMoreListener(loadMoreListener);
        new LoadMore().execute();
    }

    private class TellToFriends extends AsyncTask<Void, Void, String>
    {
        private long gameId;

        private int type;

        public TellToFriends(long gameId, int type)
        {
            super();
            this.gameId = gameId;
            this.type = type;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            dialog = new ProgressDialog(ctx);
            if (type == 0)
            {
                dialog.setMessage("Confirmando presença...");
            }
            else
            {
                dialog.setMessage("Desconfirmando presença...");
            }
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params)
        {
            switch (type)
            {
            case 0:
                return confirmation();
            case 1:
                return desconfirmation();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result != null)
            {

                if (type == 0)
                {
                    if (result.equalsIgnoreCase("OK"))
                    {
                        Toast.makeText(ctx, "Voce acabou de confirmar presenca !!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ctx, "Nao foi possivel confirmar presenca !!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    if (result.equalsIgnoreCase("OK"))
                    {
                        Toast.makeText(ctx, "Voce acabou de desconfirmar presenca !!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(ctx, "Nao foi possivel desconfirmar presenca !!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        }

        private String confirmation()
        {
            String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + Constants.CONFIRMATION + Constants.SLASH
                    + Constants.USER_ID + Constants.SLASH + gameId);
            return resposta[1];
        }

        private String desconfirmation()
        {
            String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + Constants.DESCONFIRMATION
                    + Constants.SLASH + Constants.USER_ID + Constants.SLASH + gameId);
            return resposta[1];
        }
    }

    /** Player List */
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

            return this.getPlayers(resposta[1]);
        }

        @Override
        protected void onPostExecute(List<Player> result)
        {
            super.onPostExecute(result);

            adapter = new PlayerListAdapter(ctx, result, true);
            endlessListView.setAdapter(adapter);

            adapter.addItems(result);
            endlessListView.loadMoreCompleat();
            mHaveMoreDataToLoad = true;
        }

        private List<Player> getPlayers(String text)
        {

            List<Player> players = new ArrayList<Player>();

            try
            {
                JSONArray allPlayers = new JSONArray(text);
                if (allPlayers.length() > 0)
                {
                    for (int i = 0; i < allPlayers.length(); i++)
                    {
                        try
                        {
                            JSONObject player = allPlayers.getJSONObject(i);

                            /** ID */
                            Long id = player.getLong("id");

                            /** Position */
                            String position = player.getString("position");

                            /** Rating */
                            Float rating = Float.valueOf(player.getString("rating"));

                            Player j = new Player(id, position, rating);
                            players.add(j);
                        }
                        catch (JSONException e)
                        {
                            continue;
                        }
                    }
                }
            }
            catch (JSONException e)
            {
            }
            return this.getFacebookProfile(players);
        }

        private List<Player> getFacebookProfile(List<Player> players)
        {
            Bundle params = new Bundle();

            StringBuilder ids = new StringBuilder();
            int count = 1;
            for (Player p : players)
            {
                if (count < players.size())
                {
                    ids.append(p.getId() + ",");
                }
                else
                {
                    ids.append(p.getId());
                }
                count++;
            }

            params.putString(Constants.Q, MessageFormat.format(Constants.FRIENDS_BY_ID, ids));
            Request request = new Request(Session.getActiveSession(), Constants.SLASH + Constants.FQL, params,
                    HttpMethod.GET);
            Response resp = request.executeAndWait();

            GraphObject graph = resp.getGraphObject();

            try
            {
                JSONArray friendsFromFacebook = graph.getInnerJSONObject().getJSONArray("data");

                if (friendsFromFacebook.length() > 0)
                {
                    for (int i = 0; i < friendsFromFacebook.length(); i++)
                    {
                        try
                        {
                            JSONObject player = friendsFromFacebook.getJSONObject(i);

                            /** ID */
                            Long id = Long.valueOf(player.getString(Constants.UID));

                            for (Player p : players)
                            {
                                if (p.getId() == id)
                                {
                                    p.setNome(player.getString(Constants.FIRST_NAME));
                                    p.setSobreNome(player.getString(Constants.LAST_NAME));
                                    p.setPicture(player.getString(Constants.PIC_SQUARE));
                                    break;
                                }
                            }
                        }
                        catch (JSONException e)
                        {
                            continue;
                        }
                    }
                }
                return players;
            }
            catch (JSONException e)
            {
            }
            return players;
        }
    }
}
