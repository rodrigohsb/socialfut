package br.com.socialfut.activities;

import br.com.socialfut.persistence.History;
import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 04/10/2013
 * 
 */
public class HistoryDetailsActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();

        History history = (History) bundle.getSerializable("history");

        // TODO mostrar o detalhe do historico escolhido.

    }
}
