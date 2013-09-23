package br.com.socialfut.util;

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

    private TextView mTextSureName;

    private Context ctx;

    private String firstName;

    private String sureName;

    private Response resp;

    public FacebookName(Session sessao, TextView name, TextView sureName, Context ctx)
    {
        super();
        this.session = sessao;
        this.mTextName = name;
        this.mTextSureName = sureName;
        this.ctx = ctx;
    }

    @Override
    protected Boolean doInBackground(Void... v)
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        /** Verifica se o nome consta nas preferencias */
        String fullName = sharedPrefs.getString("full_name", null);

        if (fullName != null)
        {

            String[] names = fullName.split(" ");
            this.firstName = names[0];
            this.sureName = names[1];
            return true;
        }
        else
        {
            Bundle params = new Bundle();
            params.putString("fields", "first_name,last_name");

            Request request = new Request(session, "me", params, HttpMethod.GET);
            resp = request.executeAndWait();

            /** Salva o nome nas preferencias */
            SharedPreferences.Editor editor = sharedPrefs.edit();
            GraphObject graph = resp.getGraphObject();
            firstName = graph.getProperty("first_name").toString();
            sureName = graph.getProperty("last_name").toString();

            editor.putString("full_name", firstName + " " + sureName).commit();

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean response)
    {
        super.onPostExecute(response);
        mTextName.setText(firstName);
        mTextSureName.setText(sureName);
    }
}