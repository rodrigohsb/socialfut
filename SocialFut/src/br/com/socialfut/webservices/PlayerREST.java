package br.com.socialfut.webservices;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.util.Constants;
import br.com.socialfut.util.Position;

public class PlayerREST extends AsyncTask<Void, Void, Player>
{

    private RatingBar rating;

    private TextView position;

    private int type;

    public PlayerREST()
    {
        super();
        this.type = 1;
    }

    public PlayerREST(TextView position)
    {
        super();
        this.position = position;
        this.type = 0;
    }

    public PlayerREST(RatingBar rating)
    {
        super();
        this.rating = rating;
        this.type = 3;
    }

    public PlayerREST(RatingBar rating, TextView position)
    {
        super();
        this.rating = rating;
        this.position = position;
        this.type = 2;
    }

    @Override
    protected Player doInBackground(Void... arg0)
    {
        switch (type)
        {
        case 0:
            insert();
            break;
        case 1:
            updateDevice();
            break;
        case 2:
            return getRatingAndPosition();
        case 3:
            return getRating();
        }

        return null;
    }

    /**
     * 
     * Type = 0
     * 
     * @return
     */

    private void insert()
    {
        WebServiceClient.get(Constants.URL_PLAYER_WS + "insert" + Constants.SLASH + Constants.USER_ID + Constants.SLASH
                + Constants.DEVICE_REGISTRATION_ID + Constants.SLASH + Constants.POSITION_ID);
    }

    /**
     * 
     * Type = 1
     * 
     * @return
     */
    private void updateDevice()
    {
        WebServiceClient.get(Constants.URL_PLAYER_WS + "updateDevice" + Constants.SLASH + Constants.USER_ID
                + Constants.SLASH + Constants.DEVICE_REGISTRATION_ID);
    }

    /**
     * 
     * Type = 2
     * 
     * @return
     */
    private Player getRatingAndPosition()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "getRatingAndPosition" + Constants.SLASH
                + Constants.USER_ID);

        String json = resposta[1];

        Player p = new Player();

        try
        {
            JSONObject jObject = new JSONObject(json);
            float rate = Float.valueOf(jObject.getString("rating"));
            String posicao = Position.values()[jObject.getInt("position")].toString().replace("_", " ");

            p.setRating(rate);
            p.setPosition(posicao);
        }
        catch (JSONException e)
        {
            p.setRating(0.0f);
            p.setPosition("");
        }
        return p;
    }

    /**
     * 
     * Type = 3
     * 
     * @return
     */
    private Player getRating()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "getRating" + Constants.SLASH
                + Constants.USER_ID);

        String json = resposta[1];
        Player p = new Player();

        try
        {
            JSONObject jObject = new JSONObject(json);
            float rating = Float.valueOf(jObject.getString("rating"));
            p.setRating(rating);
        }
        catch (JSONException e)
        {
            p.setRating(0);
        }
        return p;
    }

    /**
     * 
     * Type = 4
     * 
     * @return
     */
    private Player getPosition()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "getPosition" + Constants.SLASH
                + Constants.USER_ID);

        String json = resposta[0];

        Player p = new Player();

        try
        {
            JSONObject jObject = new JSONObject(json);
            String posicao = jObject.getString("posicao");
            p.setPosition(posicao);
        }
        catch (JSONException e)
        {
            p.setPosition("");
        }
        return p;
    }

    @Override
    protected void onPostExecute(Player player)
    {
        if (player != null)
        {
            if (rating != null && player.getRating() != 0)
            {
                rating.setRating(player.getRating());
            }
            if (position != null && player.getPosition() != "")
            {
                position.setText(player.getPosition());
            }
        }
        super.onPostExecute(player);
    }
}