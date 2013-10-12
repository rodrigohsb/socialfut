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

    public GameREST(Context ctx, RatingBar rating, long gameId, int type, boolean hasToShowDialog)
    {
        super();
        this.ctx = ctx;
        this.rating = rating;
        this.gameId = gameId;
        this.type = type;
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
            getRateByGame();
            break;
        case 1:
            removePlayerFromGame();
            break;
        case 2:
            addPlayerToGame();
            break;
        case 3:
            getRatesByGame();
            break;
        case 4:
            updateRating();
            break;
        case 5:
            getRates();
            break;
        case 6:
            sendConfirmation();
            break;
        case 7:
            sendDesconfirmation();
            break;
        default:
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
    private float getRateByGame()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "rateByGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            return Float.valueOf(resposta[1]);
        }
        return 0.0f;
    }

    /**
     * 
     * type = 1
     * 
     * @return
     */
    private float removePlayerFromGame()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "removePlayerFromGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Removido com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao foi possivel remover!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

    /**
     * 
     * type = 2
     * 
     * @return
     */
    private float addPlayerToGame()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "addPlayerToGame" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Voce foi escalado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao foi possivel confirmar!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

    /**
     * 
     * type = 3
     * 
     * @return
     */
    private float getRatesByGame()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "getRatesByGame" + Constants.SLASH + gameId);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "sucesso!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao foi possivel confirmar!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

    /**
     * 
     * type = 4
     * 
     * @return
     */
    private float updateRating()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "updateRating" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId + Constants.SLASH + rating);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Valor alterado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao foi possivel alterar o valor!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

    /**
     * 
     * type = 5
     * 
     * @return
     */
    private float getRates()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "rates");

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Valor alterado com sucesso!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao foi possivel alterar o valor!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

    /**
     * 
     * type = 6
     * 
     * @return
     */
    private float sendConfirmation()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "confirmation" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Confirmado!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao confirmado!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

    /**
     * 
     * type = 7
     * 
     * @return
     */
    private float sendDesconfirmation()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "desconfirmation" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + gameId);

        if (resposta[0].equals(Constants.WS_STATUS_OK))
        {
            Toast.makeText(ctx, "Desconfirmado!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(ctx, "Nao Desconfirmado!", Toast.LENGTH_SHORT).show();
        }
        return 0.0f;
    }

}
