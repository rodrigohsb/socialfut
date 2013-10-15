package br.com.socialfut.webservices;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.socialfut.util.Constants;

public class PlayerREST extends AsyncTask<Void, Void, Void>
{

    private RatingBar rating;

    private TextView position;

    private int type;

    private String deviceRegId;

    public PlayerREST(TextView position, String deviceRegId, int type)
    {
        super();
        this.position = position;
        this.type = type;
        this.deviceRegId = deviceRegId;
    }

    public PlayerREST(RatingBar rating, TextView position, int type)
    {
        super();
        this.rating = rating;
        this.position = position;
        this.type = type;
    }

    @Override
    protected Void doInBackground(Void... arg0)
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
            getRatingAndPosition();
            break;
        }

        return null;
    }

    /**
     * 
     * Type = 0
     * 
     * @return
     */

    private String insert()
    {
        WebServiceClient.get(Constants.URL_PLAYER_WS + "insert" + Constants.SLASH + Constants.USER_ID + Constants.SLASH
                + deviceRegId + Constants.SLASH + position.getText().toString());

        return "OK";
    }

    /**
     * 
     * Type = 1
     * 
     * @return
     */
    private String updateDevice()
    {
        WebServiceClient.get(Constants.URL_PLAYER_WS + "updateDevice" + Constants.SLASH + Constants.USER_ID
                + Constants.SLASH + deviceRegId);

        return "OK";
    }

    /**
     * 
     * Type = 2
     * 
     * @return
     */
    private String getRatingAndPosition()
    {

        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "getRatingAndPosition" + Constants.SLASH
                + Constants.USER_ID);

        String json = resposta[0];

        try
        {
            JSONObject jObject = new JSONObject(json);
            float rating = Float.valueOf(jObject.getString("rating"));
            String posicao = jObject.getString("posicao");

            this.position.setText(posicao);
            this.rating.setRating(rating);
        }
        catch (JSONException e)
        {
            rating.setRating(0.0f);
            position.setText("");
        }
        return "OK";
    }

    /**
     * 
     * Type = 3
     * 
     * @return
     */
    private String getRating()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "getRating" + Constants.SLASH
                + Constants.USER_ID);

        String json = resposta[0];

        try
        {
            JSONObject jObject = new JSONObject(json);
            float rating = Float.valueOf(jObject.getString("rating"));
            this.rating.setRating(rating);
        }
        catch (JSONException e)
        {
            rating.setRating(0.0f);
        }
        return "OK";
    }

    /**
     * 
     * Type = 4
     * 
     * @return
     */
    private String getPosition()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "getPosition" + Constants.SLASH
                + Constants.USER_ID);

        String json = resposta[0];

        try
        {
            JSONObject jObject = new JSONObject(json);
            String posicao = jObject.getString("posicao");
            this.position.setText(posicao);
        }
        catch (JSONException e)
        {
            position.setText("");
        }
        return "OK";
    }

}