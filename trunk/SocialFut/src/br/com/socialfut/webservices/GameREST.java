package br.com.socialfut.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RatingBar;
import android.widget.Toast;
import br.com.socialfut.util.Constants;

public class GameREST extends AsyncTask<Void, Void, Void>
{
    private Context ctx;

    private RatingBar rating;

    private long gameId;

    private int type;

    private ProgressDialog dialog;

    private boolean hasToShowDialog;

    public GameREST(Context ctx, long gameId, int type, boolean hasToShowDialog)
    {
        super();
        this.ctx = ctx;
        this.gameId = gameId;
        this.type = type;
        this.hasToShowDialog = hasToShowDialog;
    }

    public GameREST(Context ctx, long gameId, int type, RatingBar rating, boolean hasToShowDialog)
    {
        super();
        this.ctx = ctx;
        this.gameId = gameId;
        this.type = type;
        this.rating = rating;
        this.hasToShowDialog = hasToShowDialog;
    }

    @Override
    protected void onPreExecute()
    {
        if (hasToShowDialog)
        {
            dialog = new ProgressDialog(ctx);
            dialog.setMessage("Por favor, aguarde...");
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected Void doInBackground(Void... arg0)
    {
        switch (type)
        {
        case 0:
            getRatingByGame();
            break;
        case 1:
            updateRating();
            break;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if (hasToShowDialog)
        {
            dialog.dismiss();
        }
        super.onPostExecute(result);
    }

    /**
     * 
     * type = 0
     * 
     * @param
     * @return
     */
    private void getRatingByGame()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "ratingByGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        rating.setRating(3.5f);
    }

    /**
     * 
     * type = 1
     * 
     * @return
     */
    private void updateRating()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "updateRating" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId + Constants.SLASH + rating.getRating());

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Valor alterado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao foi possivel alterar o valor!", Toast.LENGTH_SHORT).show();
        }
    }
}
