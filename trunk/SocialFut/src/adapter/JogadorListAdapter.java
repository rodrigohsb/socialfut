package adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import persistence.Jogador;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.futcefet.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class JogadorListAdapter extends BaseAdapter
{

    private Context context;

    private List<Jogador> lista;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public JogadorListAdapter(Context context, List<Jogador> lista)
    {
        this.context = context;
        this.lista = lista;

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub).showImageOnFail(R.drawable.ic_stub).cacheInMemory(true)
                .cacheOnDisc(false).displayer(new RoundedBitmapDisplayer(20)).build();
    }

    private class ViewHolder
    {
        public ImageView image;

        public TextView sureName;

        public TextView name;

        public RatingBar rating;
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

        Jogador jogador = (Jogador) lista.get(position);

        View view = convertView;
        final ViewHolder holder;
        if (convertView == null)
        {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_message, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.icon);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.sureName = (TextView) view.findViewById(R.id.sureName);
            holder.rating = (RatingBar) view.findViewById(R.id.rating);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(jogador.getNome());
        holder.sureName.setText(jogador.getSobreNome());
        holder.rating.setRating(jogador.getRating());

        imageLoader.displayImage(jogador.getPicture(), holder.image, options, animateFirstListener);

        return view;
    }

    public void addItems(List<Jogador> newItems)
    {
        if (null == newItems || newItems.size() <= 0)
        {
            return;
        }

        if (null == lista)
        {
            lista = new ArrayList<Jogador>();
        }

        lista.addAll(newItems);
        notifyDataSetChanged();
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
    {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
        {
            if (loadedImage != null)
            {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay)
                {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}