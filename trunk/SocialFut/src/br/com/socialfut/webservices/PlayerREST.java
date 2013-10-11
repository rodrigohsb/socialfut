package br.com.socialfut.webservices;

import android.os.AsyncTask;
import android.widget.RatingBar;
import br.com.socialfut.util.Constants;

public class PlayerREST extends AsyncTask<RatingBar, Void, Void>
{
    private RatingBar rating;

    private float value;

    public PlayerREST(RatingBar rating)
    {
        super();
        this.rating = rating;
    }

    @Override
    protected Void doInBackground(RatingBar... params)
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "rateByUser" + "/" + Constants.USER_ID);
        // if (resposta[0].equals(Constants.WS_STATUS))
        // {
        // System.out.println(resposta[1]);
        // }
        // else
        // {
        // System.err.println(resposta[0]);
        // }

        System.out.println("resposta : " + resposta.toString());
        value = 1.0f;
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        rating.setRating(value);
    }

}