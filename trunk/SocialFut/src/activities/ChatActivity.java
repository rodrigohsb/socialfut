package activities;

import java.util.ArrayList;
import java.util.List;

import persistence.Jogador;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockActivity;


/**
 * 
 * <b>Descricao da Classe:</b><br> 
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 09/09/2013
 *
 */
public class ChatActivity extends SherlockActivity
{

    Context context;

    List<Jogador> jogadores;

    List<String> nomeJogadores = new ArrayList<String>();

    private ListView listView;

    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_lista_de_jogadores);

        context = this;

        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        listView = (ListView) findViewById(android.R.id.list);

    }
    
}


