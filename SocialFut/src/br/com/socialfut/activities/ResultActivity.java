package br.com.socialfut.activities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.persistence.Jogador;
import br.com.socialfut.util.ConstantsEnum;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ResultActivity extends SherlockActivity
{

    HashMap<String, List<Jogador>> times;

    View root;

    LinearLayout main;

    View child;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mother_sorted_players);

        main = (LinearLayout) findViewById(R.id.layout_main_sorted_players);

        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
            fill(extras);
        }
    }

    @SuppressWarnings("unchecked")
    private void fill(Bundle extras)
    {

        Map<ConstantsEnum, List<Jogador>> times2 = (Map<ConstantsEnum, List<Jogador>>) extras
                .getSerializable("selectedPlayers");

        List<Jogador> time1 = times2.get(ConstantsEnum.TIMEA);
        List<Jogador> time2 = times2.get(ConstantsEnum.TIMEB);

        Jogador jogador;

        for (int i = 0; i < time1.size() && i < time2.size(); i++)
        {
            child = (LinearLayout) getLayoutInflater().inflate(R.layout.layout_child_sorted_players, null);

            /** Jogador no time 1 */
            jogador = time1.get(i);
            TextView player0 = (TextView) child.findViewById(R.id.player0);
            player0.setText(jogador.getNome());

            /** Jogador no time 2 */
            jogador = time2.get(i);
            TextView player1 = (TextView) child.findViewById(R.id.player1);
            player1.setText(jogador.getNome());

            main.addView(child);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getSupportMenuInflater().inflate(R.menu.menu_sortear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
        case R.id.addPlayer:
            startActivity(new Intent(this, InsertOrEdit.class));
            break;

        case android.R.id.home:
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;

        case R.id.Players:
            startActivity(new Intent(this, PlayerListActivity.class));

        default:
            break;
        }

        super.onOptionsItemSelected(item);
        return true;

    }
}
