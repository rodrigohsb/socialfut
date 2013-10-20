package br.com.socialfut.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

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
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setIcon(android.R.color.transparent);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00b300")));
        // actionBar.setTitle("SocialFut");
        // actionBar.setIcon(R.drawable.icone);
    }

    public static void updateCustomActionBar(com.actionbarsherlock.app.ActionBar actionBar, String subTitle)
    {
        actionBar.setSubtitle(subTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00b300")));
    }
}
