package br.com.socialfut.webservices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import br.com.socialfut.persistence.Chat;
import br.com.socialfut.util.Constants;

public class ChatREST extends AsyncTask<Chat, Void, Void>
{

    private Context ctx;

    public ChatREST(Context ctx)
    {
        super();
        this.ctx = ctx;
    }

    @Override
    protected Void doInBackground(Chat... params)
    {
        Chat chat = params[0];

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        Constants.USER_ID = sharedPrefs.getLong("id", Constants.USER_ID);

        StringBuilder sb = new StringBuilder(Constants.URL_CHAT_WS).append(Constants.USER_ID).append(Constants.SLASH)
                .append(chat.getReceiver());

        WebServiceClient.put(sb.toString(), chat.getContent());
        return null;
    }
}
