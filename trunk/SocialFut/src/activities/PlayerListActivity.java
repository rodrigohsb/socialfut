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

        private int MAX = 20;

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
            params.putString("fields", "installed,first_name,last_name,picture");

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

                        try
                        {
                            player.getJSONObject("picture").getJSONObject("data").getString("installed");
                        }
                        catch (JSONException e)
                        {
                            continue;
                        }

                        Long id = Long.valueOf(player.getString("id"));

                        String firstName = player.getString("first_name");

                        String lastName = player.getString("last_name");

                        String url = player.getJSONObject("picture").getJSONObject("data").getString("url");

                        Jogador j = new Jogador(id, firstName, lastName, url);

                        players.add(j);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return !players.isEmpty() ? players : new ArrayList<Jogador>();
        }

        @Override
        protected void onPostExecute(List<Jogador> jogadores)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            if(!jogadores.isEmpty())
            {
                setListAdapter(new JogadorListAdapter(context, jogadores));
            }
            super.onPostExecute(jogadores);
        }
    }
}
