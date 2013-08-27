package adapter;

import java.util.List;

import persistence.Jogador;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.futcefet.R;

public class JogadorListAdapter extends BaseAdapter
{

    private View view;

    private Context context;

    private List<Jogador> lista;

    public JogadorListAdapter(Context context, List<Jogador> lista)
    {
        this.context = context;
        this.lista = lista;
    }

    public int getCount()
    {
        return lista.size();
    }

    public Object getItem(int position)
    {
        return lista.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {

        view = convertView;

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_item_message, null);
        }

        ImageView image = (ImageView) view.findViewById(R.id.icon);
        image.setImageResource(R.drawable.user_photo);

        Jogador jogador = (Jogador) lista.get(position);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setText(jogador.getNome());

        TextView sureName = (TextView) view.findViewById(R.id.sureName);
        sureName.setText(jogador.getSobreNome());

        RatingBar rating = (RatingBar) view.findViewById(R.id.rating);
        rating.setRating(jogador.getRating());

        return view;
    }
}