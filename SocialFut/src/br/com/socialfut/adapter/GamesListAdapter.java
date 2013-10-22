package br.com.socialfut.adapter;

import java.text.DateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.util.SquareImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 03/10/2013
 * 
 */
public class GamesListAdapter extends BaseAdapter
{
    private List<Game> games;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context;

    private String[] pictures = new String[] {
            "http://2.bp.blogspot.com/_0AzeTeHDPbU/SibXejBzezI/AAAAAAAAAB4/IWpNY3ZU6Os/s320/campo-de-futebol-infantil.jpg",
            "http://www.castro.pr.gov.br/site/images/stories/Campo_Futebol_Society.jpg",
            "http://images03.olx.com.br/ui/11/73/87/1309874233_223606687_2-Fotos-de--Grama-sintetica-preco-quadra-de-futebol-campo-de-futebol-society.jpg",
            "http://caxixa.com/2011/wp-content/uploads/2011/01/campos-de-futebol-7.1_640x480.jpg",
            "http://www.greenvisionbrasil.com.br/fmanager/green/categorias/foto12_1.jpg",
            "http://images03.olx.com.br/ui/4/12/70/1379431896_546891270_11-Chacara-em-Cabreuva-3-quartos-otimo-estado-com-piscina-mini-campo-de-futebol-.jpg" };

    public GamesListAdapter(Context context, List<Game> games)
    {
        this.context = context;
        this.games = games;

        if (!imageLoader.isInited())
        {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub).showImageOnFail(R.drawable.ic_stub).cacheInMemory(true)
                .cacheOnDisc(true).build();
    }

    @Override
    public int getCount()
    {
        return games.size();
    }

    @Override
    public Object getItem(int position)
    {
        return games.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Game game = (Game) games.get(position);
        View view = convertView;
        final ViewHolder holder;

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid2, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) view.findViewById(R.id.text);
            holder.image = (SquareImageView) view.findViewById(R.id.picture);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        imageLoader.displayImage(pictures[(int) (Math.random() * 5)], holder.image, options, animateFirstListener);
        holder.name.setText(game.getTitle() + "\n"
                + DateFormat.getDateInstance(DateFormat.MEDIUM).format(game.getCreatedDate()));
        return view;
    }

    private class ViewHolder
    {
        public ImageView image;

        public TextView name;
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