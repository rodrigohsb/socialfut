package br.com.socialfut.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.socialfut.R;
import br.com.socialfut.adapter.ChatListAdapter;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
 * @since 25/09/2013
 * 
 */
public class ChatListActivity extends SherlockListActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateActionBar(getSupportActionBar());

        Session session = Session.getActiveSession();
        if (session != null && (session.getState().isOpened()))
        {
            FacebookFriends faceFriends = new FacebookFriends(session);
            faceFriends.execute();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        Player Jogador = (Player) getListAdapter().getItem(position);

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("from", Jogador.getNome() + " " + Jogador.getSobreNome());
        intent.putExtra("userId", String.valueOf(Jogador.getId()));

        startActivity(intent);
        finish();
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
            Intent intent = new Intent(this, DrawerLayoutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Session session = Session.getActiveSession();
            if (session != null && (session.getState().isOpened()))
            {
                FacebookFriends faceFriends = new FacebookFriends(session);
                faceFriends.execute();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private class FacebookFriends extends AsyncTask<Void, String, List<Player>>
    {
        private Session session;

        private Response resp;

        private final ProgressDialog dialog = new ProgressDialog(ChatListActivity.this);

        private AlertDialog alertDialog;

        public FacebookFriends(Session sessao)
        {
            super();
            this.session = sessao;
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Buscando amigos no Facebook...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected List<Player> doInBackground(Void... v)
        {

            if (Constants.jogadores != null)
            {
                return Constants.jogadores;
            }

            Bundle params = new Bundle();
            params.putString(Constants.Q, Constants.FRIENDS_USES_APP);

            Request request = new Request(session, Constants.SLASH + Constants.FQL, params, HttpMethod.GET);
            resp = request.executeAndWait();

            GraphObject graph = resp.getGraphObject();

            List<Player> players = this.getFriends(graph);

            if (!players.isEmpty())
            {
                Constants.jogadores = players;
            }

            return players;
        }

        @Override
        protected void onPostExecute(List<Player> jogadores)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            if (!jogadores.isEmpty())
            {
                setListAdapter(new ChatListAdapter(ChatListActivity.this, jogadores));
            }
            else
            {
                android.content.DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        alertDialog.dismiss();
                        startActivity(new Intent(ChatListActivity.this, DrawerLayoutActivity.class));
                        finish();
                    }
                };

                alertDialog = new AlertUtils(ChatListActivity.this).getAlertDialog(Constants.WARNING,
                        Constants.NO_FRIEND, positiveButton, null);

                alertDialog.show();

            }
            super.onPostExecute(jogadores);
        }

        /**
         * 
         * Obtem todos os amigos que tem o aplicativo.
         * 
         * @param graph
         * @return
         */
        private List<Player> getFriends(GraphObject graph)
        {

            List<Player> players = new ArrayList<Player>();

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

                            if (Boolean.parseBoolean(player.getString(Constants.IS_APP_USER)))
                            {
                                /** ID */
                                Long id = Long.valueOf(player.getString(Constants.UID));

                                /** Primeiro Nome */
                                String firstName = player.getString(Constants.FIRST_NAME);

                                /** Primeiro Nome */
                                String lastName = player.getString(Constants.LAST_NAME);

                                /** Foto */
                                String url = player.getString(Constants.PIC_SQUARE);

                                Player j = new Player(id, firstName, lastName, url);
                                players.add(j);
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
                return null;
            }

        }

    }
}
