package br.com.socialfut.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import br.com.socialfut.R;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.sort.Sort;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.ConstantsEnum;

import com.actionbarsherlock.app.SherlockListActivity;

public class PlayerListForSort extends SherlockListActivity implements OnClickListener
{
    List<String> nomeJogadores = new ArrayList<String>();

    private ListView listView;

    private ArrayAdapter<Player> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lista_de_jogadores);

        listView = (ListView) findViewById(android.R.id.list);
        ActionBar.updateActionBar(getSupportActionBar());
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

        Map<ConstantsEnum, List<Player>> times = new HashMap<ConstantsEnum, List<Player>>();
        times = Sort.getOrder(selectedItems);
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("selectedPlayers", (Serializable) times);
        intent.putExtras(b);
        startActivity(intent);
    }
}
