package br.com.socialfut.activities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.ExpandableListView;
import br.com.socialfut.R;
import br.com.socialfut.adapter.ExpandableListAdapter;
import br.com.socialfut.database.GameDB;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.persistence.Rate;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Constants;
import br.com.socialfut.util.Group;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class GameHistoryActivity extends SherlockActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar.updateCustomActionBar(getSupportActionBar(), "Histórico");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_hitories);

        SparseArray<Group> groups = getHistories();

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.listView);
        ExpandableListAdapter adapter = new ExpandableListAdapter(this, groups);
        listView.setAdapter(adapter);

    }

    public SparseArray<Group> getHistories()
    {
        SparseArray<Group> groups = new SparseArray<Group>();
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(Constants.HOUR_PATTERN_FOR_USER);

        GameDB gameDB = new GameDB(this);
        // RateDB rateDB = new RateDB(this);

        for (int i = 1; i < 16; i++)
        {
            Game g = new Game("Titulo " + i, "Rua " + i, new Date(), new Date(), new Date());
            Rate r = new Rate(g.getId(), 0, 0);

            gameDB.saveGame(g);
            // rateDB.saveRate(r);
        }

        List<Game> games = gameDB.getAllGames();

        int i = 0;
        for (Game game : games)
        {
            Group group = new Group(game.getTitle() + " - "
                    + new SimpleDateFormat(Constants.DATE_PATTERN_FOR_USER).format(game.getCreatedDate()));
            group.children.add("Endereço: " + game.getAddress());
            group.children.add("Horário: " + dateFormat2.format(game.getStartDate()) + " - "
                    + dateFormat2.format(game.getStartDate()));

            // double rate = rateDB.getRateByGameId(game);

            group.children.add("Qualificação recebida: " + 4.5);
            groups.append(i++, group);
        }

        return groups;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.menu_lista_de_jogadores_sortear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
        case android.R.id.home:
            Intent intent = new Intent(this, DrawerLayoutActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
