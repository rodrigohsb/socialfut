package activities;

import persistence.Jogador;
import persistence.Jogador.Jogadores;
import util.AlertUtils;
import util.Constants;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import database.Repositorio;

public class InsertOrEdit extends SherlockActivity
{

    private AlertDialog dialog;

    private Context context;

    private String errorMessage = "";

    private static int MAX_TEL = 8;

    private Long id;

    private EditText nomeJogador;

    private EditText sobreNomeJogador;

    private EditText posicaoJogador;

    private EditText telJogador;

    private RatingBar rating;

    private Repositorio repositorio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_cadastro_jogador);

        context = this;

        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        repositorio = new Repositorio(context);

        id = null;

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            id = extras.getLong(Jogadores._ID);

            if (id != null)
            {
                Jogador jogador = buscarJogador(id);

                nomeJogador.setText(jogador.getNome());
                sobreNomeJogador.setText(jogador.getSobreNome());
                posicaoJogador.setText(jogador.getPosition());
                telJogador.setText(jogador.getTel());
                rating.setRating(jogador.getRating());
            }
        }

        findViewById(R.id.salvar_cadastro).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                nomeJogador = (EditText) findViewById(R.id.nome_cadastro);

                sobreNomeJogador = (EditText) findViewById(R.id.sobreNome_cadastro);

                posicaoJogador = (EditText) findViewById(R.id.posicao_cadastro);

                telJogador = (EditText) findViewById(R.id.telefone_cadastro);

                rating = (RatingBar) findViewById(R.id.qualificacao_cadastro);

                /** Formata o telefone */
                String telefone = formatador(telJogador);

                float value = rating.getRating();

                /** Valida os campos */
                boolean validou = validaCampos(nomeJogador.getText().toString(), sobreNomeJogador.getText().toString(),
                        telefone, rating.getRating());

                if (validou)
                {

                    Jogador jogador = new Jogador(nomeJogador.getText().toString(), sobreNomeJogador.getText()
                            .toString(), posicaoJogador.getText().toString(), value, telefone);

                    salvarJogador(jogador);

                    String message = getResources().getString(R.string.add_more_player);
                    flowSucess(message);

                }
                else
                {
                    flowError(errorMessage);
                }
            }
        });

    }

    /**
     * 
     * Fluxo de sucesso
     * 
     * @param sucessMessage
     */
    private void flowSucess(String sucessMessage)
    {

        OnClickListener positiveButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                clear();
            }
        };

        OnClickListener negativeButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                clear();
                startActivity(new Intent(context, PlayerListActivity.class));
            }
        };

        dialog = new AlertUtils(context)
                .getAlertDialog(Constants.SUCESS, sucessMessage, positiveButton, negativeButton);

        dialog.show();

    }

    /**
     * 
     * Limpa os itens da tela
     * 
     */
    private void clear()
    {
        nomeJogador.setText("");
        sobreNomeJogador.setText("");
        telJogador.setText("");
        rating.setRating(0);
    }

    /**
     * 
     * Fluxo de erro
     * 
     * @param sucessMessage
     */
    private void flowError(String errorMessage)
    {

        OnClickListener positiveButton = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
            }
        };

        dialog = new AlertUtils(context).getAlertDialog(Constants.ERRO, errorMessage, positiveButton, null);

        dialog.show();

    }

    /**
     * 
     * Formata o telefone
     * 
     * @param telJogador
     * @return
     */
    private String formatador(EditText telJogador)
    {

        String telefone = telJogador.getText().toString().replaceAll("\\(", "").replaceAll("\\)", "")
                .replaceAll("-", "").replaceAll(" ", "");

        return telefone;
    }

    /**
     * 
     * Valida os campos inseridos pele usuario
     * 
     * @param nome
     * @param sobreNome
     * @param telefone
     * @param value
     */
    private boolean validaCampos(String nome, String sobreNome, String telefone, float value)
    {

        boolean allOk = true;

        if (nome.trim().equalsIgnoreCase(""))
        {
            allOk = false;
            errorMessage = "Por favor, preencha o nome do jogador";
        }
        else if (sobreNome.trim().equalsIgnoreCase(""))
        {
            allOk = false;
            errorMessage = "Por favor, preencha o sobrenome do jogador";
        }
        else if (value == 0)
        {
            allOk = false;
            errorMessage = "Por favor, preencha o rating do jogador";
        }
        else if (telefone.trim().equalsIgnoreCase(""))
        {
            allOk = false;
            errorMessage = "Por favor, preencha o telefone do jogador";
        }
        else if (telefone.trim().length() < MAX_TEL)
        {
            allOk = false;
            errorMessage = "Por favor, preencha o telefone do jogador corretamente";
        }

        return allOk;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.menu_cadastro_jogador, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
        case R.id.subtitle:
            startActivity(new Intent(this, LegendaActivity.class));
            break;
        case android.R.id.home:
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        default:
            break;
        }
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // Cancela para não ficar nada na tela pendente
        setResult(RESULT_CANCELED);

        // Fecha a tela
        finish();
    }

    public void salvar()
    {

        Jogador jogador = new Jogador();
        if (id != null)
        {
            jogador.setId(id);
        }
        jogador.setNome(nomeJogador.getText().toString());
        jogador.setPosition(posicaoJogador.getText().toString());
        jogador.setRating(rating.getRating());
        jogador.setSobreNome(sobreNomeJogador.getText().toString());
        jogador.setTel(telJogador.getText().toString());

        // Salvar
        salvarJogador(jogador);

        // OK
        setResult(RESULT_OK, new Intent());

        // Fecha a tela
        finish();
    }

    public void excluir()
    {
        if (id != null)
        {
            excluirJogador(id);
        }

        // OK
        setResult(RESULT_OK, new Intent());

        // Fecha a tela
        finish();
    }

    protected Jogador buscarJogador(long id)
    {
        return repositorio.buscarJogador(id);
    }

    protected void salvarJogador(Jogador jogador)
    {
        repositorio.salvar(jogador);
    }

    protected void excluirJogador(long id)
    {
        repositorio.deletar(id);
    }

}
