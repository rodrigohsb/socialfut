package br.com.socialfut.gcm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import br.com.socialfut.activities.ChatActivity;
import br.com.socialfut.activities.GameDetailsActivity;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.util.ActivityStackUtils;
import br.com.socialfut.util.Constants;
import br.com.socialfut.util.NotificationUtil;
import br.com.socialfut.webservices.WebServiceClient;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;

public class GCMIntentService extends GCMBaseIntentService
{

    private static final String TAG = "GCMIntentService";

    private Context ctx;
    
    public GCMIntentService()
    {
        super(Constants.PROJECT_NUMBER);
    }

    @Override
    protected void onRegistered(Context context, String registrationId)
    {
        Constants.DEVICE_REGISTRATION_ID = registrationId;
    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
    {
    }

    @Override
    public void onError(Context context, String errorId)
    {
    }

    @Override
    protected void onMessage(Context context, Intent intent)
    {
        
        ctx = this;
        
        try
        {
            String fullMsg = intent.getStringExtra("msg");

            Log.i(TAG, "Mensagem recebida " + fullMsg);
            String[] text = fullMsg.split(Constants.SEMICOLON);

            /** Id do Usuario que enviou a mensagem */
            String facebookId = text[0];

            /** Conteudo da mensagem */
            String msgContent = text[1];

            Player j = null;
            
            Bitmap bitmap = null;
            
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            
            if (sharedPrefs.contains(facebookId))
            {
                Gson gson = new Gson();
                String json = sharedPrefs.getString(facebookId, "");
                j = gson.fromJson(json, Player.class);
            }
            else
            {
                j = getFacebookData(facebookId);

                Editor prefsEditor = sharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(j);
                prefsEditor.putString(facebookId, json);
                prefsEditor.commit();
            }
            
            bitmap = getBitmap(j.getPicture());

            if (text.length == 3)
            {
                if (msgContent.equalsIgnoreCase(Constants.CONFIRMATION))
                {
                    /** Confirmacao */
                    enviarMensagemParaApp("Confirmou presenca na partida!", Long.valueOf(text[2]), j.getNome() + " " + j.getSobreNome(), bitmap, GameDetailsActivity.class);
                }
                else if (msgContent.equalsIgnoreCase(Constants.DESCONFIRMATION))
                {
                    /** Desconfirmacao */
                    enviarMensagemParaApp("Desconfirmou presenca na partida!", Long.valueOf(text[2]), j.getNome() + " " + j.getSobreNome(), bitmap, GameDetailsActivity.class);
                }
                else if (msgContent.equalsIgnoreCase(Constants.INVITATION))
                {
                    /** Convite */
                    enviarMensagemParaApp("Parabens, voce foi convidado para uma partida!", Long.valueOf(text[2]), j.getNome() + " " + j.getSobreNome(), bitmap, GameDetailsActivity.class);
                }
            }
            else
            {
                /** Chat */
                enviarMensagemParaApp(msgContent, facebookId, j.getNome() + " " + j.getSobreNome(), bitmap, ChatActivity.class);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Obtem os dados do usuario para mostrar na Notificacao.
     * 
     * @param msg
     * @return
     * @throws JSONException
     */
    private Player getFacebookData(String facebookId) throws JSONException
    {

        Bundle params = new Bundle();
        params.putString("fields", "first_name,last_name,picture.type(large)");

        Request request = new Request(Session.getActiveSession(), facebookId, params, HttpMethod.GET);
        Response response = request.executeAndWait();

        JSONObject object = response.getGraphObject().getInnerJSONObject();

        /** Primeiro Nome */
        String firstName = object.getString("first_name");

        /** Ultimo Nome */
        String lastName = object.getString("last_name");

        /** Foto */
        String urlPicture = object.getJSONObject("picture").getJSONObject("data").getString("url");

        Player j = new Player(Long.valueOf(facebookId), firstName, lastName, urlPicture);

        return j;
    }

    private Bitmap getBitmap(String urlPicture) throws IOException
    {
        URL myFileUrl;
        InputStream is = null;
        myFileUrl = new URL(urlPicture);

        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setDoInput(true);
        conn.connect();
        is = conn.getInputStream();
        return BitmapFactory.decodeStream(is);
    }

    private void enviarMensagemParaApp(String msg, long gameId, String from, Bitmap bitmap, Class<?> cls)
    {
        Log.i(TAG, "Mensagem recebida " + msg);

        // Cria a notificacao e informa para abrir a activity de entrada
        String resposta[] = WebServiceClient.get(Constants.URL_GAME_WS + "gameById" + Constants.SLASH + gameId);

        Gson g = new Gson();
        Game game = g.fromJson(resposta[1], Game.class);
        
        Intent intent = new Intent(this, cls);
        intent.putExtra("game", game);
        
        NotificationUtil.generateNotification(this, msg, intent, from, bitmap);
    }

    private void enviarMensagemParaApp(String msg, String facebookId, String from, Bitmap bitmap, Class<?> cls)
    {
        Log.i(TAG, "Mensagem recebida " + msg);

        // Cria a notificacao e informa para abrir a activity de entrada
        Intent intent = new Intent(this, cls);
        intent.putExtra("msg", msg);
        intent.putExtra("from", from);
        intent.putExtra("userId", facebookId);
        NotificationUtil.generateNotification(this, msg, intent, from, bitmap);
    }

    private void enviarMensagemParaApp(String msg)
    {
        Log.i(TAG, msg);

        // Cria a notificacao e informa para abrir a activity de entrada
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("msg", msg);
        NotificationUtil.generateNotification(this, msg, intent);
    }

    private void reconfiguraActivity()
    {
        // Se a notificacao esta aberta
        if (ActivityStackUtils.isMyApplicationTaskOnTop(this))
        {
            // Dispara uma Intent para o receiver configurado na Activity
            Intent intent = new Intent("RECEIVER_QUE_VAI_RECEBER_ESTA_MSG");
            intent.putExtra("refresh", true);
            sendBroadcast(intent);
        }
    }
}
