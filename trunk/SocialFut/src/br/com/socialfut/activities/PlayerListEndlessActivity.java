package br.com.socialfut.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import br.com.socialfut.R;
import br.com.socialfut.adapter.PlayerListAdapter;
import br.com.socialfut.drawer.EndlessListView;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.util.ActionBar;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 02/09/2013
 * 
 */
public class PlayerListEndlessActivity extends SherlockActivity
{
    public static final int INSERIR_EDITAR = 1;

    public static final int BUSCAR = 2;

    private Context context;

    private List<Player> jogadores;

    private EndlessListView endlessListView;

    private int mCount;

    private boolean mHaveMoreDataToLoad;

    private PlayerListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.layout_endless_list);

        ActionBar.updateActionBar(getSupportActionBar());

        adapter = new PlayerListAdapter(context, jogadores,12,true);

        mCount = 0;
        mHaveMoreDataToLoad = true;
        endlessListView = (EndlessListView) findViewById(R.id.endless);

        endlessListView.setAdapter(adapter);
        endlessListView.setOnLoadMoreListener(loadMoreListener);

    }

    private void loadMoreData()
    {
        new LoadMore().execute((Void) null);
    }

    private EndlessListView.OnLoadMoreListener loadMoreListener = new EndlessListView.OnLoadMoreListener()
    {

        @Override
        public boolean onLoadMore()
        {
            if (true == mHaveMoreDataToLoad)
            {
                loadMoreData();
            }
            return mHaveMoreDataToLoad;
        }
    };

    private class LoadMore extends AsyncTask<Void, Void, List<Player>>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mCount++;
        }

        @Override
        protected List<Player> doInBackground(Void... params)
        {
            List<Player> jogadores = new ArrayList<Player>();

            try
            {
                Thread.sleep(2112);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            return jogadores;
        }

        @Override
        protected void onPostExecute(List<Player> result)
        {
            super.onPostExecute(result);

            adapter.addItems(result);
            endlessListView.loadMoreCompleat();
            mHaveMoreDataToLoad = mCount < 7;
        }
    }

}
