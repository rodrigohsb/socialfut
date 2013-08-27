package activities;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import br.com.futcefet.R;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 19/08/2013
 * 
 */
public class AddNewPlayerActivity extends SherlockActivity
{

    Context ctx;

    private static final String[] NAMES = new String[] { "João", "José", "Rafael", "Rafaela", "Rodrigo", "Guilherme",
            "Thiago", "Tiago", "Fernando", "Francisco", "Italo", "Germano" };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        ctx = this;
        setContentView(R.layout.layout_add_player);
        getSupportActionBar().setIcon(R.drawable.icone);
        getSupportActionBar().setTitle("SocialFut");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));

        setContentView(R.layout.layout_add_player);
        super.onCreate(savedInstanceState);

        final Button b = (Button) findViewById(R.id.ask_for_friend);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                NAMES);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.players_list);
        textView.setAdapter(adapter);

        textView.setOnItemClickListener(new OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId)
            {
                String selection = (String) parent.getItemAtPosition(position);
                b.setEnabled(true);
                Toast.makeText(ctx, selection, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
