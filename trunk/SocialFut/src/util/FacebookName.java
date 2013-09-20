package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class FacebookName extends AsyncTask<Void, String, Boolean>
{
    private Session session;

    private TextView mTextName;

    private Context ctx;

    private String name;

    private Response resp;

    public FacebookName(Session sessao, TextView txt, Context ctx)
    {
        super();
        this.session = sessao;
        this.mTextName = txt;
        this.ctx = ctx;
    }

    @Override
    protected Boolean doInBackground(Void... v)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        /** Verifica se o nome consta nas preferencias */
        String name = sharedPrefs.getString("name", null);

        if (name != null)
        {
            this.name = name;
            return true;
        }
        else
        {
            Bundle params = new Bundle();
            params.putString("fields", "name");

            Request request = new Request(session, "me", params, HttpMethod.GET);
            resp = request.executeAndWait();

            /** Salva o nome nas preferencias */
            SharedPreferences.Editor editor = sharedPrefs.edit();
            GraphObject graph = resp.getGraphObject();
            name = graph.getProperty("name").toString();
            this.name = name;
            
            int id = Integer.valueOf(graph.getProperty("id").toString());
            editor.putString("name", name).commit();

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean response)
    {
        super.onPostExecute(response);
        mTextName.setText(name);
    }
}