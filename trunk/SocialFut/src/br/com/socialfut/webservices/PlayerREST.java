package br.com.socialfut.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.RatingBar;
import br.com.socialfut.util.Player;

public class PlayerREST extends AsyncTask<Void, Void, Void>
{

    private Context ctx;

    private RatingBar rating;

    private String deviceRegId;

    private Integer position;

    private int type;

    private ProgressDialog dialog;

    private boolean hasToShowDialog;

    public PlayerREST(Context ctx, RatingBar rating, String deviceRegId, Integer position, int type,
            ProgressDialog dialog, boolean hasToShowDialog)
    {
        super();
        this.ctx = ctx;
        this.rating = rating;
        this.deviceRegId = deviceRegId;
        this.position = position;
        this.type = type;
        this.dialog = dialog;
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
            getPlayer();
            break;
        case 1:
            insert();
            break;
        case 2:
            updateDevice();
            break;
        case 3:
            updateRating();
            break;
        case 4:
            getRating();
            break;
        case 5:
            getPosition();
            break;
        default:
            break;
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        if (hasToShowDialog && dialog.isShowing())
        {
            dialog.dismiss();
        }
        super.onPostExecute(result);
    }

    /**
     * 
     * Type = 0
     * 
     * @return
     */
    private Player getPlayer()
    {
        return null;
    }

    /**
     * 
     * Type = 1
     * 
     * @return
     */

    private String insert()
    {
        return "OK";
    }

    /**
     * 
     * Type = 2
     * 
     * @return
     */
    private String updateDevice()
    {
        return "OK";
    }

    /**
     * 
     * Type = 3
     * 
     * @return
     */
    private String updateRating()
    {
        return "OK";
    }

    /**
     * 
     * Type = 4
     * 
     * @return
     */
    private String getRating()
    {
        return "OK";
    }

    /**
     * 
     * Type = 5
     * 
     * @return
     */
    private String getPosition()
    {
        return "OK";
    }
}