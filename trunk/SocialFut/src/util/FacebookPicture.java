package util;

import helper.ImageHelper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class FacebookPicture extends AsyncTask<Void, String, Bitmap>
{
    private Session session;

    private ImageView imgAvatar;

    private Bitmap bitmap;

    public FacebookPicture(Session sessao, ImageView img)
    {
        super();
        this.session = sessao;
        this.imgAvatar = img;
    }

    @Override
    protected Bitmap doInBackground(Void... v)
    {
        Bundle params = new Bundle();
        params.putString("fields", "name,picture");

        Request request = new Request(session, "me", params, HttpMethod.GET);
        Response response = request.executeAndWait();

        GraphObject graph = response.getGraphObject();

        try
        {
            String url = graph.getInnerJSONObject().getJSONObject("picture").getJSONObject("data").getString("url");

            URL myFileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        imgAvatar.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bitmap));
    }
}