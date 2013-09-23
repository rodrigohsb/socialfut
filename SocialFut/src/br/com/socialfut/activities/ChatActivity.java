package br.com.socialfut.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.util.ActionBar;

import com.actionbarsherlock.app.SherlockActivity;
import com.google.android.gcm.GCMRegistrar;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propÃ³sito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 09/09/2013
 * 
 */
public class ChatActivity extends SherlockActivity
{
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_chat);

        ActionBar.updateActionBar(getSupportActionBar());

        context = this;

        TextView text = (TextView) findViewById(R.id.tMsgRecebida);
        text.setText("Oi!");

        // Configura o BroadcastReceiver para receber mensagens
        registerReceiver(mensagemReceiver, new IntentFilter("RECEIVER_QUE_VAI_RECEBER_ESTA_MSG"));

        // Se existe alguma mensagem enviada pela Notification, recebe aqui
        String msg = getIntent().getStringExtra("msg");
        Bitmap from = (Bitmap) getIntent().getParcelableExtra("from");

        if (msg != null)
        {
            exibirMensagem(msg, from);
        }

    }

    // Receiver para receber a mensagem do Service por Intent
    private final BroadcastReceiver mensagemReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String msg = intent.getExtras().getString("msg");
            Bitmap from = (Bitmap) intent.getParcelableExtra("from");

            if (msg != null && from != null)
            {
                exibirMensagem(msg, from);
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        // Cancela o receiver e encerra o serviço do GCM
        unregisterReceiver(mensagemReceiver);
        GCMRegistrar.onDestroy(context);
        super.onDestroy();
    }

    private void exibirMensagem(String msg, Bitmap bitmap)
    {
        if (bitmap == null)
        {
            TextView text = (TextView) findViewById(R.id.tMsgRecebida);
            text.append(msg + "\n------------------------\n");
        }
        else
        {
            // TODO Colocar a foto do participante
            TextView text = (TextView) findViewById(R.id.tMsgRecebida);
            text.setTextSize(20);
            text.append(msg + "\n------------------------\n");
        }

    }
}
