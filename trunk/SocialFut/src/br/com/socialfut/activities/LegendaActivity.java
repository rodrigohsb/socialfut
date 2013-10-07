package br.com.socialfut.activities;

import android.os.Bundle;
import br.com.socialfut.R;
import br.com.socialfut.util.ActionBar;

import com.actionbarsherlock.app.SherlockActivity;

public class LegendaActivity extends SherlockActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActionBar.updateActionBar(getSupportActionBar());
        setContentView(R.layout.layout_subtitle);
    }
}
