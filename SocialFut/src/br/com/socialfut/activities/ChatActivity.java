package br.com.socialfut.activities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.socialfut.R;
import br.com.socialfut.adapter.ChatAdapter;
import br.com.socialfut.database.Repositorio;
import br.com.socialfut.helper.MessageData;
import br.com.socialfut.persistence.Chat;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Sender;

import com.actionbarsherlock.app.SherlockListActivity;
import com.google.android.gcm.GCMRegistrar;

public class ChatActivity extends SherlockListActivity
{

    private Repositorio repo;

    private ChatAdapter adapter;

    private List<MessageData> msgs = new ArrayList<MessageData>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = getIntent().getExtras();

        String from = bundle.getString("from");
        String msg = bundle.getString("msg");
        Long facebookId = Long.valueOf(bundle.getString("userId"));

        ActionBar.updateCustomActionBar(getSupportActionBar(), from);

        adapter = new ChatAdapter(this, msgs);
        setListAdapter(adapter);

        Button send = (Button) findViewById(R.id.send_button);

        repo = new Repositorio(this);

        if (msg != null)
        {
            save(facebookId, msg);
            updateHistory(facebookId);
            exibirMensagem(msg, Sender.OTHER);
        }

        send.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                EditText message = (EditText) findViewById(R.id.enter_message);
                String mText = message.getText().toString();

                if (!mText.trim().equals(""))
                {
                    save(123l, mText);
                    exibirMensagem(mText, Sender.ME);
                    message.setText("");
                }
            }
        });

        // Configura o BroadcastReceiver para receber mensagens
        // registerReceiver(receiver, new
        // IntentFilter("RECEIVER_QUE_VAI_RECEBER_ESTA_MSG"));
        //
        // // Se existe alguma mensagem enviada pela Notification, recebe aqui
        // String msg = getIntent().getStringExtra("msg");
        // // Long from = Long.valueOf(getIntent().getStringExtra("from"));
        //
        // // if (msg != null && from != 0)
        // if (msg != null)
        // {
        //
        // save(100000422142423l, msg);
        // updateHistory(100000422142423l);
        // exibirMensagem(msg, Sender.OTHER);
        // }

    }

    // Receiver para receber a mensagem do Service por Intent
    private final BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String msg = intent.getExtras().getString("msg");
            String from = intent.getExtras().getString("from");
            Long userId = Long.valueOf(intent.getExtras().getString("userId"));

            save(userId, msg);
            updateHistory(userId);

            // Monta a actionBar com o nome do usuario
            ActionBar.updateCustomActionBar(getSupportActionBar(), from);

            exibirMensagem(msg, Sender.OTHER);
        }
    };

    @Override
    protected void onDestroy()
    {
        // Cancela o receiver e encerra o servi�o do GCM
        unregisterReceiver(receiver);
        GCMRegistrar.onDestroy(this);
        super.onDestroy();
    }

    /**
     * 
     * Mensagem recebida ou enviada pelo usuario
     * 
     * @param msg
     */
    private void exibirMensagem(String msg, Sender sender)
    {
        this.msgs.add(new MessageData(msg, sender));
        adapter.notifyDataSetChanged();
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

        int i = 0;

        for (Chat c : chatList)
        {
            if (i % 2 == 0)
            {
                msgs.add(new MessageData(c.getContent(), Sender.ME));
            }
            else
            {
                msgs.add(new MessageData(c.getContent(), Sender.OTHER));
            }
            i++;
        }
        adapter.notifyDataSetChanged();
    }

    private void save(Long from, String msg)
    {
        java.util.Date date = new java.util.Date();
        Chat chat = new Chat(from, msg, new Timestamp(date.getTime()));
        repo.save(chat);

    }
}