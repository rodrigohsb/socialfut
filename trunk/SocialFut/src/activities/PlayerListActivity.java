package activities;

import java.util.List;

import persistence.Jogador;
import adapter.JogadorListAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import database.Repositorio;

public class PlayerListActivity extends SherlockListActivity
{
    public static final int INSERIR_EDITAR = 1;

    public static final int BUSCAR = 2;

    private Context context;

    private static Repositorio repositorio;

    private List<Jogador> jogadores;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this;

        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        atualizarLista();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);
        editarJogador(position);
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

    /**
     * 
     * Busca todos os jogadores cadastrados
     * 
     */
    protected void atualizarLista()
    {
        repositorio = new Repositorio(context);

        jogadores = repositorio.listarJogadores();
        setListAdapter(new JogadorListAdapter(context, jogadores));
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
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
        case INSERIR_EDITAR:

            startActivityForResult(new Intent(this, InsertOrEdit.class), INSERIR_EDITAR);
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
            atualizarLista();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        repositorio.fechar();
    }
}
