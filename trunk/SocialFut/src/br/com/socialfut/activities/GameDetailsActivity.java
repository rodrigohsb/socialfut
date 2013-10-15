package br.com.socialfut.activities;

import java.text.SimpleDateFormat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import br.com.socialfut.R;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.util.ActionBar;
import br.com.socialfut.util.Constants;
import br.com.socialfut.webservices.GameREST;
import br.com.socialfut.webservices.WebServiceClient;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente proposito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 04/10/2013
 * 
 */
public class GameDetailsActivity extends SherlockActivity
{

    private SimpleDateFormat dateFormat;

    private ToggleButton toggleButton;

    private Context ctx;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Detalhes da Partida");

        setContentView(R.layout.layout_game_details);

        ctx = this;

        Bundle bundle = getIntent().getExtras();
        game = (Game) bundle.getSerializable("game");

        dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN_FOR_USER);

        /** Titulo */
        TextView titleGameDetails = (TextView) findViewById(R.id.titleGameDetails);
        titleGameDetails.setText(game.getTitle());

        /** Criada em */
        TextView createdDateGameDetails = (TextView) findViewById(R.id.createdDateGameDetails);
        createdDateGameDetails.setText(dateFormat.format(game.getCreatedDate()));

        /** Dia a ser disputada */
        TextView dateGameDetails = (TextView) findViewById(R.id.dateGameDetails);
        dateGameDetails.setText(dateFormat.format(game.getStartDate()));

        dateFormat = new SimpleDateFormat(Constants.HOUR_PATTERN);

        /** Horario */
        TextView hourGameDetails = (TextView) findViewById(R.id.hourGameDetails);
        hourGameDetails.setText(dateFormat.format(game.getStartDate()) + " - "
                + dateFormat.format(game.getFinishDate()));

        /** Endereco */
        TextView addressGameDetails = (TextView) findViewById(R.id.addressGameDetails);
        addressGameDetails.setText(game.getAddress());

        RatingBar rating = (RatingBar) findViewById(R.id.ratingBarGameDetails);

        // AsyncTask p/ preenche o rating
        new GameREST(ctx, game.getId(), 0, rating, false).execute();

        toggleButton = (ToggleButton) findViewById(R.id.toggleButtonGameDetails);

        if (bundle.getString("old") != null)
        {
            toggleButton.setVisibility(View.GONE);
        }

        toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    sendConfirmation();
                }
                else
                {
                    sendDesconfirmation();
                }
            }

        });
    }

    private void sendConfirmation()
    {
        ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setMessage("Confirmando presenca...");
        dialog.setCancelable(false);
        dialog.show();

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "confirmation" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + game.getId());

        dialog.dismiss();
        
        if (resposta[0] == "OK")
        {
            Toast.makeText(this, "Voce acabou de confirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Nao foi possivel confirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDesconfirmation()
    {
        ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setMessage("Desconfirmando presenca...");
        dialog.setCancelable(false);
        dialog.show();

        String[] resposta = WebServiceClient.get(Constants.URL_GAME_WS + "desconfirmation" + Constants.SLASH
                + Constants.USER_ID + Constants.SLASH + game.getId());

        dialog.dismiss();
        
        if (resposta[0] == "OK")
        {
            Toast.makeText(this, "Voce acabou de desconfirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Nao foi possivel desconfirmar presenca !!", Toast.LENGTH_SHORT).show();
        }
    }
}
