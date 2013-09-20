package util;

import helper.ImageHelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.ImageView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class FacebookPicture extends AsyncTask<Void, String, Bitmap>
{
    private Session session;

    private ImageView imgAvatar;

    private Bitmap bitmap;

    private Context ctx;

    public FacebookPicture(Session sessao, ImageView img, Context ctx)
    {
        super();
        this.session = sessao;
        this.imgAvatar = img;
        this.ctx = ctx;
    }

    @Override
    protected Bitmap doInBackground(Void... v)
    {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        /** Verifica se o nome consta nas preferencias */
        String previouslyEncodedImage = sharedPrefs.getString("image", null);

        if (previouslyEncodedImage != null)
        {
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            return bitmap;
        }
        Bundle params = new Bundle();
        params.putString("fields", "picture");

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

            /** Grava a imagem */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("image", encodedImage).commit();

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