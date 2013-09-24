package br.com.socialfut.gcm;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import br.com.socialfut.activities.ChatActivity;
import br.com.socialfut.persistence.Jogador;
import br.com.socialfut.util.ActivityStackUtils;
import br.com.socialfut.util.Constants;
import br.com.socialfut.util.NotificationUtil;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService
{

    private static final String TAG = "GCMIntentService";

    public GCMIntentService()
    {
        super(Constants.PROJECT_NUMBER);
    }

    @Override
    protected void onRegistered(Context context, String registrationId)
    {
        enviarMensagemParaApp("Device registrado: registrationId = " + registrationId);

        reconfiguraActivity();
    }

    @Override
    protected void onUnregistered(Context context, String registrationId)
    {
        enviarMensagemParaApp("Device removido do registro.");

        reconfiguraActivity();
    }

    @Override
    public void onError(Context context, String errorId)
    {
        enviarMensagemParaApp("Erro: " + errorId);
    }

    @Override
    protected void onMessage(Context context, Intent intent)
    {
        String fullMsg = intent.getStringExtra("msg");
        String[] text = fullMsg.split(";");

        /** Id do Usuario que enviou a mensagem */
        String facebookId = text[0];

        /** Conteudo da mensagem */
        String msgContent = text[1];

        /** Obtem a foto do usuario */
        Jogador j = getFacebookData(facebookId);

        Bitmap bitmap = getBitmap(j.getPicture());

        enviarMensagemParaApp(msgContent, j.getNome() + " " + j.getSobreNome(), bitmap);
    }

    /**
     * 
     * Obtem a imagem do usuario para mostrar na Notificacao
     * 
     * @param msg
     * @return
     */
    private Jogador getFacebookData(String facebookId)
    {

        Jogador j = null;

        Bundle params = new Bundle();
        params.putString("fields", "first_name,last_name,picture.type(large)");

        Request request = new Request(Session.getActiveSession(), facebookId, params, HttpMethod.GET);
        Response response = request.executeAndWait();

        GraphObject graph = response.getGraphObject();

        try
        {
            JSONObject object = graph.getInnerJSONObject();

            /** Primeiro Nome */
            String firstName = object.getString("first_name");

            /** Ultimo Nome */
            String lastName = object.getString("last_name");

            /** Foto */
            String urlPicture = object.getJSONObject("picture").getJSONObject("data").getString("url");

            j = new Jogador(Long.valueOf(facebookId), firstName, lastName, urlPicture);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return j;
    }

    private Bitmap getBitmap(String urlPicture)
    {
        URL myFileUrl;
        InputStream is = null;
        try
        {
            myFileUrl = new URL(urlPicture);

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setInstanceFollowRedirects(false);
            conn.setDoInput(true);
            conn.connect();
            is = conn.getInputStream();
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return BitmapFactory.decodeStream(is);
    }

    private void enviarMensagemParaApp(String msg, String from, Bitmap bitmap)
    {
        Log.i(TAG, msg);

        // Cria a notifica��o, e informa para abrir a activity de entrada
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("msg", msg);
        intent.putExtra("from", bitmap);
        NotificationUtil.generateNotification(this, msg, intent, from, bitmap);
    }

    private void enviarMensagemParaApp(String msg)
    {
        Log.i(TAG, msg);

        // Cria a notifica��o, e informa para abrir a activity de entrada
        Intent intent2 = new Intent(this, ChatActivity.class);
        intent2.putExtra("msg", msg);
        NotificationUtil.generateNotification(this, msg, intent2);
    }

    private void reconfiguraActivity()
    {
        // Se a aplica��o est� aberta
        if (ActivityStackUtils.isMyApplicationTaskOnTop(this))
        {
            // Dispara uma Intent para o receiver configurado na Activity
            Intent intent = new Intent("RECEIVER_QUE_VAI_RECEBER_ESTA_MSG");
            intent.putExtra("refresh", true);
            sendBroadcast(intent);
        }
    }
}