package util;

import java.util.List;

import org.json.JSONException;

import persistence.Jogador;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

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

    public FacebookFriends(Session sessao, TextView txt, Context ctx)
    {
        super();
        this.session = sessao;
        this.ctx = ctx;
    }

    @Override
    protected List<Jogador> doInBackground(Void... v)
    {
        Bundle params = new Bundle();
        params.putString("fields", "friends");

        Request request = new Request(session, "me", params, HttpMethod.GET);
        resp = request.executeAndWait();

        GraphObject graph = resp.getGraphObject();

        try
        {
            String url = graph.getInnerJSONObject().getJSONObject("data").getJSONObject("data").getString("url");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Jogador> jogadores)
    {
        super.onPostExecute(jogadores);
    }
}