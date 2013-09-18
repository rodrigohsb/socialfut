package util;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class FacebookName extends AsyncTask<Void, String, Response>
{
    private Session session;

    private TextView mTextName;

    public FacebookName(Session sessao, TextView txt)
    {
        super();
        this.session = sessao;
        this.mTextName = txt;
    }

    @Override
    protected Response doInBackground(Void... v)
    {
        Bundle params = new Bundle();
        params.putString("fields", "name,picture");

        Request request = new Request(session, "me", params, HttpMethod.GET);

        return request.executeAndWait();
    }

    @Override
    protected void onPostExecute(Response response)
    {
        super.onPostExecute(response);

        GraphObject graph = response.getGraphObject();
        String name = graph.getProperty("name").toString();

        if (mTextName != null)
        {
            mTextName.setText(name);
        }
    }
}