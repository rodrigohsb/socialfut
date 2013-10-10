package br.com.socialfut.webservices;

import android.os.AsyncTask;
import br.com.socialfut.persistence.Chat;
import br.com.socialfut.util.Constants;

public class ChatREST extends AsyncTask<Chat, Void, String[]>
{
    @Override
    protected String[] doInBackground(Chat... params)
    {
        Chat chat = params[0];

        StringBuilder sb = new StringBuilder(Constants.URL_GCM_WS).append("chat").append(Constants.SLASH)
                .append(Constants.USER_ID).append(Constants.SLASH).append(chat.getReceiver()).append(Constants.SLASH)
                .append(chat.getContent());

        String[] resposta = WebServiceClient.get(sb.toString());

        if (resposta[0].equals(Constants.WS_STATUS))
        {
            System.out.println(resposta[1]);
        }
        else
        {
            System.err.println(resposta[0]);
        }
        return resposta;
    }

}
