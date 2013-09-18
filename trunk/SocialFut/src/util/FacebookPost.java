package util;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;

public class FacebookPost extends AsyncTask<Void, Void, Void>
{
    private Session session;

    private EditText edtPost;

    public FacebookPost(Session sessao, EditText edtPost)
    {
        super();
        this.session = sessao;
        this.edtPost = edtPost;
    }

    @Override
    protected Void doInBackground(Void... v)
    {
        Bundle params = new Bundle();
        params.putString("message", String.valueOf(edtPost.getText()));

        Request request = new Request(session, "me/feed", params, HttpMethod.POST);
        request.executeAndWait();
        edtPost.setText("");
        return null;
    }
}