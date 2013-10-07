package br.com.socialfut.activities;

import java.text.SimpleDateFormat;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        ActionBar.updateCustomActionBar(getSupportActionBar(), "Detalhes da Partida");

        setContentView(R.layout.layout_game_details);

        Bundle bundle = getIntent().getExtras();
        Game game = (Game) bundle.getSerializable("game");

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

        RatingBar ratingGameDetails = (RatingBar) findViewById(R.id.ratingBarGameDetails);
        ratingGameDetails.setRating(game.getRate());

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
                    Toast.makeText(GameDetailsActivity.this, "Você acabou de confirmar presença !!", Toast.LENGTH_SHORT)
                            .show();
                }
                else
                {
                    Toast.makeText(GameDetailsActivity.this, "Você acabou de desistir !!", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
