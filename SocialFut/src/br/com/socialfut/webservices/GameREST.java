package br.com.socialfut.webservices;

import java.util.Date;

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

    private String title;

    private String address;

    private Date startDate;

    private Date finishDate;

    public GameREST(Context ctx, String title, String address, Date startDate, Date finishDate)
    {
        super();
        this.ctx = ctx;
        this.type = 0;
        this.hasToShowDialog = true;
        this.title = title;
        this.address = address;
        this.startDate = startDate;
        this.finishDate = finishDate;
    }

    public GameREST(Context ctx, long gameId)
    {
        super();
        this.ctx = ctx;
        this.gameId = gameId;
        this.type = 1;
        this.hasToShowDialog = true;
    }

    public GameREST(Context ctx, long gameId, RatingBar rating)
    {
        super();
        this.ctx = ctx;
        this.gameId = gameId;
        this.type = 3;
        this.rating = rating;
        this.hasToShowDialog = false;
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
            createGame();
            break;
        case 1:
            getOldGames();
            break;
        case 2:
            getNewGames();
            break;
        case 3:
            getRatingByGame();
            break;
        case 4:
            updateRating();
            break;
        }
        return null;
    }

    /**
     * 
     * type = 0
     * 
     * @param
     * @return
     */
    private void createGame()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "createGame" + Constants.SLASH + title
                + Constants.SLASH + address + Constants.SLASH + startDate + Constants.SLASH + finishDate);
    }

    /**
     * 
     * type = 1
     * 
     * @param
     * @return
     */
    private void getOldGames()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "ratingByGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);
    }

    /**
     * 
     * type = 2
     * 
     * @param
     * @return
     */
    private void getNewGames()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "ratingByGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);
    }

    /**
     * 
     * type = 3
     * 
     * @param
     * @return
     */
    private void getRatingByGame()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "ratingByGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        rating.setRating(Float.valueOf(resposta[1]));
    }

    /**
     * 
     * type = 4
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

    @Override
    protected void onPostExecute(Void result)
    {
        if (hasToShowDialog)
        {
            dialog.dismiss();
        }
        super.onPostExecute(result);
    }
}
