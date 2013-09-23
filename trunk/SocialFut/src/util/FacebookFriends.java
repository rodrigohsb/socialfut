package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import persistence.Jogador;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class FacebookFriends extends AsyncTask<Void, String, List<Jogador>>
{
    private Session session;

    private Context ctx;

    private Response resp;

    private static int MAX = 100;

    public FacebookFriends(Session sessao, Context ctx)
    {
        super();
        this.session = sessao;
        this.ctx = ctx;
    }

    @Override
    protected List<Jogador> doInBackground(Void... v)
    {

        List<Jogador> players = new ArrayList<Jogador>();

        Bundle params = new Bundle();
        params.putString("fields", "installed,first_name,last_name");

        Request request = new Request(session, "me/friends", params, HttpMethod.GET);
        resp = request.executeAndWait();

        GraphObject graph = resp.getGraphObject();

        try
        {
            JSONArray friendsFromFacebook = graph.getInnerJSONObject().getJSONObject("friends").getJSONArray("data");

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

                    Jogador j = new Jogador(id, primeiroNome, sobreNome, "");
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
        super.onPostExecute(jogadores);
    }
}