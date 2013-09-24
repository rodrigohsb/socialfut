package br.com.socialfut.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.socialfut.R;
import br.com.socialfut.adapter.ChatAdapter;
import br.com.socialfut.helper.MessageData;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Sender;

import com.actionbarsherlock.app.SherlockListActivity;
import com.google.android.gcm.GCMRegistrar;

public class ChatActivity extends SherlockListActivity
{

    private Context ctx;

    ChatAdapter adapter;

    List<MessageData> msgs = new ArrayList<MessageData>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar.updateActionBar(getSupportActionBar());

        adapter = new ChatAdapter(this, msgs);
        setListAdapter(adapter);

        Button send = (Button) findViewById(R.id.send_button);
        send.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                exibirMensagem();
            }
        });

        // Configura o BroadcastReceiver para receber mensagens
        registerReceiver(mensagemReceiver, new IntentFilter("RECEIVER_QUE_VAI_RECEBER_ESTA_MSG"));

        // Se existe alguma mensagem enviada pela Notification, recebe aqui
        String msg = getIntent().getStringExtra("msg");

        if (msg != null)
        {
            exibirMensagem(msg, Sender.OTHER);
        }

    }

    // Receiver para receber a mensagem do Service por Intent
    private final BroadcastReceiver mensagemReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String msg = intent.getExtras().getString("msg");

            if (msg != null)
            {
                exibirMensagem(msg, Sender.OTHER);
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        // Cancela o receiver e encerra o serviço do GCM
        unregisterReceiver(mensagemReceiver);
        GCMRegistrar.onDestroy(ctx);
        super.onDestroy();
    }

    /**
     * 
     * Mensagem enviada pelo usuario
     * 
     * @param msg
     */
    private void exibirMensagem(String msg, Sender sender)
    {
        msgs.add(new MessageData(msg, sender));
        adapter.notifyDataSetChanged();
    }

    /** Mensagem enviada pelo usuario */
    private void exibirMensagem()
    {
        EditText message = (EditText) findViewById(R.id.enter_message);
        String mText = message.getText().toString();

        if (!mText.trim().equals(""))
        {
            exibirMensagem(mText, Sender.ME);
            message.setText("");
        }
    }
}