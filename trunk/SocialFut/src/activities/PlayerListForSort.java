package activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistence.Jogador;
import sort.Sort;
import util.Constants;
import util.ConstantsEnum;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;

import database.Repositorio;

public class PlayerListForSort extends SherlockListActivity implements OnClickListener
{
    public static final int INSERIR_EDITAR = 1;

    public static final int BUSCAR = 2;

    Context context;

    private static Repositorio repositorio;

    List<Jogador> jogadores;

    List<String> nomeJogadores = new ArrayList<String>();

    private ListView listView;

    private Handler mHandler;

    private EditText timepicker;

    private final int DEFAULT_MAX = 22;

    private final int DEFAULT_MIN = 4;

    private ArrayAdapter<Jogador> adapter;

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
        Button button = (Button) findViewById(R.id.testbutton);

        repositorio = new Repositorio(context);

        atualizarLista();

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

    protected void editarJogador(int posicao)
    {

        Jogador jogador = jogadores.get(posicao);

        Intent it = new Intent(context, InsertOrEdit.class);

        it.putExtra(persistence.Jogador.Jogadores._ID, jogador.getId());
        startActivityForResult(it, INSERIR_EDITAR);
    }

    protected void atualizarLista()
    {
        jogadores = repositorio.listarJogadores();

        for (Jogador j : jogadores)
        {
            nomeJogadores.add(j.getNome() + " " + j.getSobreNome());
        }

        adapter = new ArrayAdapter<Jogador>(context, android.R.layout.simple_list_item_multiple_choice, jogadores);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId())
        {
        case INSERIR_EDITAR:

            startActivityForResult(new Intent(this, InsertOrEdit.class), INSERIR_EDITAR);
            break;

        case android.R.id.home:
            Intent intent = new Intent(this, HomeActivity.class);
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
            atualizarLista();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        repositorio.fechar();
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
        ArrayList<Jogador> selectedItems = new ArrayList<Jogador>();

        for (int i = 0; i < checked.size(); i++)
        {
            int position = checked.keyAt(i);
            if (checked.valueAt(i))
            {
                selectedItems.add(adapter.getItem(position));
            }
        }

        Map<ConstantsEnum, List<Jogador>> times2 = new HashMap<ConstantsEnum, List<Jogador>>();
        times2 = Sort.getOrder(selectedItems);
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("selectedPlayers", (Serializable) times2);
        intent.putExtras(b);
        startActivity(intent);

    }
}
