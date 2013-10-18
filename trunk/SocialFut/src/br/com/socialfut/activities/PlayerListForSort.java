package br.com.socialfut.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import br.com.socialfut.R;
import br.com.socialfut.adapter.PlayerListAdapter;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.sort.Sort;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Constants;
import br.com.socialfut.util.ConstantsEnum;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class PlayerListForSort extends SherlockListActivity implements OnClickListener
{
    public static final int INSERIR_EDITAR = 1;

    Context context;

    List<String> nomeJogadores = new ArrayList<String>();

    private ListView listView;

    private Handler mHandler;

    private EditText timepicker;

    private final int DEFAULT_MAX = 22;

    private final int DEFAULT_MIN = 4;

    private ArrayAdapter<Player> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lista_de_jogadores);

        context = this;

        listView = (ListView) findViewById(android.R.id.list);
        Button button = (Button) findViewById(R.id.testbutton);

        ActionBar.updateActionBar(getSupportActionBar());

        Session session = Session.getActiveSession();
        if (session != null && (session.getState().isOpened()))
        {
            FacebookFriends faceFriends = new FacebookFriends(session);
            faceFriends.execute();
        }

        /** Cria o handler e mostra o dialog depois 700ms */
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 700);

        button.setOnClickListener(this);
    }

    private final Runnable mRunnable = new Runnable()
    {
        public void run()
        {
            View view = getLayoutInflater().inflate(R.layout.layout_picker, null);

            timepicker = (EditText) view.findViewById(R.id.timepicker_input);
            final ImageButton mais = (ImageButton) view.findViewById(R.id.increment);
            final ImageButton menos = (ImageButton) view.findViewById(R.id.decrement);

            mais.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    updatePicker(getCurrent() + 2);
                }
            });

            mais.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    return false;
                }
            });

            menos.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    updatePicker(getCurrent() - 2);
                }
            });

            menos.setOnLongClickListener(new OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    return false;
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Quantos jogadores?");
            builder.setView(view);
            builder.setCancelable(false);
            builder.setPositiveButton(Constants.OK, new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
    };

    private void updatePicker(int current)
    {
        if (!(current < DEFAULT_MIN || current > DEFAULT_MAX))
        {
            updateView(current);
        }
    }

    private void updateView(int current)
    {
        timepicker.setText(String.valueOf(current));
    }

    private int getCurrent()
    {
        return Integer.valueOf(timepicker.getText().toString());
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            Intent intent = new Intent(this, DrawerLayoutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {
            Session session = Session.getActiveSession();
            if (session != null && (session.getState().isOpened()))
            {
                FacebookFriends faceFriends = new FacebookFriends(session);
                faceFriends.execute();
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
    }

    @Override
    public void onClick(View v)
    {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<Player> selectedItems = new ArrayList<Player>();

        for (int i = 0; i < checked.size(); i++)
        {
            int position = checked.keyAt(i);
            if (checked.valueAt(i))
            {
                selectedItems.add(adapter.getItem(position));
            }
        }

        Map<ConstantsEnum, List<Player>> times2 = new HashMap<ConstantsEnum, List<Player>>();
        times2 = Sort.getOrder(selectedItems);
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("selectedPlayers", (Serializable) times2);
        intent.putExtras(b);
        startActivity(intent);

    }

    private class FacebookFriends extends AsyncTask<Void, String, List<Player>>
    {
        private Session session;

        private Response resp;

        private int MAX = 10;

        private final ProgressDialog dialog = new ProgressDialog(PlayerListForSort.this);

        public FacebookFriends(Session sessao)
        {
            super();
            this.session = sessao;
        }

        @Override
        protected void onPreExecute()
        {
            dialog.setMessage("Buscando amigos no Facebook...");
            dialog.show();
        }

        @Override
        protected List<Player> doInBackground(Void... v)
        {

            List<Player> players = new ArrayList<Player>();

            Bundle params = new Bundle();
            params.putString("fields", "picture,first_name,last_name");

            Request request = new Request(session, "me/friends?limit=" + MAX, params, HttpMethod.GET);
            resp = request.executeAndWait();

            GraphObject graph = resp.getGraphObject();

            try
            {
                JSONArray friendsFromFacebook = graph.getInnerJSONObject().getJSONArray("data");

                if (friendsFromFacebook.length() > 0)
                {
                    for (int i = 0; i < friendsFromFacebook.length(); i++)
                    {
                        JSONObject player = friendsFromFacebook.getJSONObject(i);

                        /** ID */
                        Long id = Long.valueOf(player.getString("id"));

                        /** Primeiro Nome */
                        String firstName = player.getString("first_name");

                        /** Primeiro Nome */
                        String lastName = player.getString("last_name");

                        /** Foto */
                        String url = player.getJSONObject("picture").getJSONObject("data").getString("url");

                        Player j = new Player(id, firstName, lastName, url);
                        players.add(j);
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return !players.isEmpty() ? players : null;
        }

        @Override
        protected void onPostExecute(List<Player> jogadores)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
            setListAdapter(new PlayerListAdapter(PlayerListForSort.this, jogadores));
            super.onPostExecute(jogadores);
        }
    }
}
