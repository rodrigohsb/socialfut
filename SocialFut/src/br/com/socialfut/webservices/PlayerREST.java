package br.com.socialfut.webservices;

import android.os.AsyncTask;
import android.widget.RatingBar;
import br.com.socialfut.util.Constants;

public class PlayerREST extends AsyncTask<RatingBar, Void, String[]>
{

    private RatingBar rating;

    public PlayerREST(RatingBar rating)
    {
        super();
        this.rating = rating;
    }

    @Override
    protected String[] doInBackground(RatingBar... params)
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "rateByUser" + "/" + Constants.USER_ID);
        if (resposta[0].equals(Constants.WS_STATUS))
        {
            System.out.println(resposta[0]);
        }
        else
        {
            System.err.println(resposta[1]);
        }
        return resposta;
    }

    @Override
    protected void onPostExecute(String[] result)
    {
        super.onPostExecute(result);
        rating.setRating(Float.valueOf(result[0]));
    }

}