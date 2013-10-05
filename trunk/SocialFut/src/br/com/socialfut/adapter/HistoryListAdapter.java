package br.com.socialfut.adapter;

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
import br.com.socialfut.persistence.History;
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
public class HistoryListAdapter extends BaseAdapter
{
    private List<History> histories;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context;

    public HistoryListAdapter(Context context, List<History> histories)
    {
        this.context = context;
        this.histories = histories;

        imageLoader.init(ImageLoaderConfiguration.createDefault(context));

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub)
                .showImageForEmptyUri(R.drawable.ic_stub).showImageOnFail(R.drawable.ic_stub).cacheInMemory(true)
                .cacheOnDisc(false).build();
    }

    @Override
    public int getCount()
    {
        return histories.size();
    }

    @Override
    public Object getItem(int position)
    {
        return histories.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        History history = (History) histories.get(position);
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

        imageLoader.displayImage(history.getPicture(), holder.image, options, animateFirstListener);
        holder.name.setText(history.getTitle());
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