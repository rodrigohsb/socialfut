package br.com.socialfut.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.socialfut.R;
import br.com.socialfut.adapter.ChatListAdapter;
import br.com.socialfut.persistence.Jogador;
import br.com.socialfut.util.ActionBar;
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
 * TODO Explicar detalhadamente propósito da classe.
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

        Jogador Jogador = (Jogador) getListAdapter().getItem(position);

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

    private class FacebookFriends extends AsyncTask<Void, String, List<Jogador>>
    {
        private Session session;

        private Response resp;

        private int MAX = 10;

        private final ProgressDialog dialog = new ProgressDialog(ChatListActivity.this);

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
        protected List<Jogador> doInBackground(Void... v)
        {

            List<Jogador> players = new ArrayList<Jogador>();

            // Somente usa o "Cache" se tiver houve algum registro.
            if (Constants.jogadores != null)
            {
                return Constants.jogadores;
            }

            Bundle params = new Bundle();
            params.putString("fields", "picture,first_name,last_name");

            // Request request = new Request(session, "me/friends, params,HttpMethod.GET);
            Request request = new Request(session, "me/friends?limit=" + MAX, params, HttpMethod.GET);
            resp = request.executeAndWait();

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
                            Long id = Long.valueOf(player.getString("id"));

                            /** Primeiro Nome */
                            String firstName = player.getString("first_name");

                            /** Primeiro Nome */
                            String lastName = player.getString("last_name");

                            /** Foto */
                            String url = player.getJSONObject("picture").getJSONObject("data").getString("url");

                            Jogador j = new Jogador(id, firstName, lastName, url);
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
                return null;
            }

            Constants.jogadores = players;

            return players;
        }

        @Override
        protected void onPostExecute(List<Jogador> jogadores)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            setListAdapter(new ChatListAdapter(ChatListActivity.this, jogadores));
            super.onPostExecute(jogadores);
        }
    }
}
