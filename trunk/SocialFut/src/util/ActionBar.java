package util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import br.com.futcefet.R;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 20/09/2013
 * 
 */
public class ActionBar
{

    public static void updateActionBar(com.actionbarsherlock.app.ActionBar actionBar)
    {
        actionBar.setIcon(R.drawable.icone);
        actionBar.setTitle("SocialFut");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));
    }
}