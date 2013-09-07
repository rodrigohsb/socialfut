package activities;

import java.util.List;

import persistence.Jogador;
import teste.EndlessListView;
import adapter.JogadorListAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockActivity;

import database.Repositorio;

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

    private static Repositorio repositorio;

    private List<Jogador> jogadores;

    private EndlessListView endlessListView;

    private int mCount;

    private boolean mHaveMoreDataToLoad;

    private JogadorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.layout_endless_list);
        this.createActionBar();

        repositorio = new Repositorio(context);

        jogadores = repositorio.listarJogadores();
        adapter = new JogadorListAdapter(context, jogadores);

        mCount = 0;
        mHaveMoreDataToLoad = true;
        endlessListView = (EndlessListView) findViewById(R.id.endless);

        endlessListView.setAdapter(adapter);
        endlessListView.setOnLoadMoreListener(loadMoreListener);

    }

    private void createActionBar()
    {
        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));
    }

    /**
     * 
     * Chama a Activity para editar o jogador passando o id
     * 
     * @param posicao
     */
    protected void editarJogador(int posicao)
    {

        Jogador jogador = jogadores.get(posicao);

        Intent it = new Intent(context, InsertOrEdit.class);

        it.putExtra(persistence.Jogador.Jogadores._ID, jogador.getId());
        startActivityForResult(it, INSERIR_EDITAR);
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

    private class LoadMore extends AsyncTask<Void, Void, List<Jogador>>
    {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mCount++;
        }

        @Override
        protected List<Jogador> doInBackground(Void... params)
        {
            List<Jogador> jogadores = repositorio.listarJogadores();

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
        protected void onPostExecute(List<Jogador> result)
        {
            super.onPostExecute(result);

            adapter.addItems(result);
            endlessListView.loadMoreCompleat();
            mHaveMoreDataToLoad = mCount < 7;
        }
    }

}
