package br.com.socialfut.activities;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.database.ChatDB;
import br.com.socialfut.persistence.Chat;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Constants;
import br.com.socialfut.webservices.ChatREST;

import com.actionbarsherlock.app.SherlockActivity;

public class ChatActivity extends SherlockActivity
{

    private EditText messageText;

    private ViewGroup messagesContainer;

    private ScrollView scrollContainer;

    private Long facebookId;

    private ChatDB repo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        repo = new ChatDB(this);

        Bundle bundle = getIntent().getExtras();

        String from = bundle.getString("from");
        facebookId = Long.valueOf(bundle.getString("userId"));

        /** Customiza ActionBar */
        ActionBar.updateCustomActionBar(getSupportActionBar(), from);

        String msg = bundle.getString("msg");

        if (msg != null)
        {
            save(facebookId, Constants.USER_ID, msg);
        }

        messagesContainer = (ViewGroup) findViewById(R.id.messagesContainer);
        scrollContainer = (ScrollView) findViewById(R.id.scrollContainer);
        messageText = (EditText) findViewById(R.id.messageEdit);

        findViewById(R.id.sendButton).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!messageText.getText().toString().trim().equals(""))
                {
                    sendMessage(messageText.getText().toString());
                }
            }
        });

        /** Busca o historico para mostrar na tela */
        updateHistory(facebookId);

    }

    private void sendMessage(String message)
    {
        save(Constants.USER_ID, facebookId, message);
        showMessage(message, false);
        messageText.setText("");
        new ChatREST().execute(new Chat(Constants.USER_ID, facebookId, message));
    }

    private void showMessage(String message, boolean leftSide)
    {
        final TextView textView = new TextView(ChatActivity.this);
        textView.setTextColor(Color.BLACK);
        textView.setText(message);

        int bgRes = R.drawable.left_message_bg;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        if (!leftSide)
        {
            bgRes = R.drawable.right_message_bg;
            params.gravity = Gravity.RIGHT;
        }

        textView.setLayoutParams(params);

        textView.setBackgroundResource(bgRes);

        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                messagesContainer.addView(textView);

                // Scroll to bottom
                if (scrollContainer.getChildAt(0) != null)
                {
                    scrollContainer.scrollTo(scrollContainer.getScrollX(), scrollContainer.getChildAt(0).getHeight());
                }
                scrollContainer.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    /**
     * 
     * Mostra o historico
     * 
     * @param facebookId
     */
    private void updateHistory(long userId)
    {
        List<Chat> chatList = repo.searchHistory(userId);

        for (Chat c : chatList)
        {
            if (c.getSender() == Constants.USER_ID)
            {
                showMessage(c.getContent(), false);
            }
            else
            {
                showMessage(c.getContent(), true);
            }
        }
    }

    /**
     * 
     * Salva no banco a msg reccebida ou enviada
     * 
     * @param from
     * @param to
     * @param msg
     */
    private void save(long from, long to, String msg)
    {
        Chat chat = new Chat(from, to, msg);
        repo.save(chat);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, ChatListActivity.class));
        finish();
    }
}