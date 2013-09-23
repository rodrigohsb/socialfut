package br.com.socialfut.activities;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import br.com.socialfut.R;
import br.com.socialfut.adapter.ChatAdapter;
import br.com.socialfut.helper.MessageData;
import br.com.socialfut.util.ActionBar;

import com.actionbarsherlock.app.SherlockListActivity;

public class ChatActivity2 extends SherlockListActivity
{

    ChatAdapter adapter;

    List<MessageData> msgs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar.updateActionBar(getSupportActionBar());

        msgs = new ArrayList<MessageData>();
        adapter = new ChatAdapter(this, msgs);
        setListAdapter(adapter);

        Button send = (Button) findViewById(R.id.send_button);
        send.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                EditText message = (EditText) findViewById(R.id.enter_message);
                String mText = message.getText().toString();

                msgs.add(new MessageData(mText));
                adapter.notifyDataSetChanged();
                message.setText("");
            }
        });
    }

}