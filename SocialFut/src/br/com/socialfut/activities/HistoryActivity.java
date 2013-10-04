package br.com.socialfut.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import br.com.socialfut.R;
import br.com.socialfut.adapter.HistoryListAdapter;
import br.com.socialfut.persistence.History;
import br.com.socialfut.util.ActionBar;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 04/10/2013
 * 
 */
public class HistoryActivity extends SherlockActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateActionBar(getSupportActionBar());

        setContentView(R.layout.grid1);
        final GridView gridView = (GridView) findViewById(R.id.gridview);

        List<History> histories = new ArrayList<History>();

        for (int i = 0; i < 20; i++)
        {
            History h = new History(
                    "Title " + i,
                    "http://2.bp.blogspot.com/_0AzeTeHDPbU/SibXejBzezI/AAAAAAAAAB4/IWpNY3ZU6Os/s320/campo-de-futebol-infantil.jpg",
                    null, null, null, null);
            histories.add(h);
        }

        gridView.setAdapter(new HistoryListAdapter(this, histories));

        gridView.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                gridView.getAdapter().getItem(position);
                Toast.makeText(HistoryActivity.this, "Opa!", Toast.LENGTH_SHORT).show();
            }
        });
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
            Intent intent = new Intent(this, MainFragment.class);
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
