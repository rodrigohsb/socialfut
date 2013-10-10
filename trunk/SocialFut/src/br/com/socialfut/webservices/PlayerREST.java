package br.com.socialfut.webservices;

import android.os.AsyncTask;
import br.com.socialfut.util.Constants;

public class PlayerREST extends AsyncTask<Void, Void, String[]>
{
    @Override
    protected String[] doInBackground(Void... params)
    {
        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "insert" + "/" + Constants.USER_ID + "/"
                + Constants.DEVICE_REGISTRATION_ID);
        // if (resposta[0].equals(Constants.WS_STATUS))
        // {
        // System.out.println(resposta[0]);
        // }
        // else
        // {
        // System.err.println(resposta[1]);
        // }
        return resposta;
    }

}