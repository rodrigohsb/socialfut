package activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import persistence.Jogador;
import util.ActionBar;
import adapter.JogadorListAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class PlayerListActivity extends SherlockListActivity
{
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;

        ActionBar.updateActionBar(getSupportActionBar());

        Session session = Session.getActiveSession();
        if (session != null && (session.getState().isOpened()))
        {
            FacebookFriends ff = new FacebookFriends(session, context);
            ff.execute();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
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
            Intent intent = new Intent(this, HomeActivity.class);
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
                FacebookFriends ff = new FacebookFriends(session, context);
                ff.execute();
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

        private Context ctx;

        private Response resp;

        private int MAX = 50;

        private final ProgressDialog dialog = new ProgressDialog(PlayerListActivity.this);

        public FacebookFriends(Session sessao, Context ctx)
        {
            super();
            this.session = sessao;
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Buscando amigos no Facebook...");
            dialog.show();
        }

        @Override
        protected List<Jogador> doInBackground(Void... v)
        {

            List<Jogador> players = new ArrayList<Jogador>();

            Bundle params = new Bundle();
            params.putString("fields", "picture,name");

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
                        JSONObject player = friendsFromFacebook.getJSONObject(i);
                        Long id = Long.valueOf(player.getString("id"));

                        String nomeCompleto = player.getString("name");
                        String[] nomes = nomeCompleto.split(" ");

                        String primeiroNome;
                        String sobreNome;

                        if (nomes.length == 2)
                        {
                            primeiroNome = nomes[0];
                            sobreNome = nomes[1];
                        }
                        else
                        {
                            primeiroNome = nomes[0] + " " + nomes[1];
                            sobreNome = nomes[nomes.length - 1];
                        }

                        String url = player.getJSONObject("picture").getJSONObject("data").getString("url");
                        Jogador j = new Jogador(id, primeiroNome, sobreNome, url);
                        players.add(j);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return !players.isEmpty() ? players : null;
        }

        @Override
        protected void onPostExecute(List<Jogador> jogadores)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            setListAdapter(new JogadorListAdapter(context, jogadores));
            super.onPostExecute(jogadores);
        }
    }
}
