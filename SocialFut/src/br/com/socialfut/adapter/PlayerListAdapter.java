package br.com.socialfut.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.persistence.Player;
import br.com.socialfut.util.AlertUtils;
import br.com.socialfut.util.Constants;
import br.com.socialfut.webservices.PlayerREST;
import br.com.socialfut.webservices.WebServiceClient;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PlayerListAdapter extends BaseAdapter
{

    private Context context;

    private List<Player> lista;

    private DisplayImageOptions options;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private boolean hasPositionAndQualification;

    private ActionMode mActionMode;

    private long gameId;

    private List<Long> ids = new ArrayList<Long>();

    private AlertDialog alertDialog;

    private Activity act;

    public PlayerListAdapter(Context context, List<Player> lista, long gameId, boolean hasPositionAndQualification)
    {
        this.context = context;
        this.lista = lista;
        this.gameId = gameId;

        act = (Activity) context;

        System.out.println(lista.toString());

        if (!imageLoader.isInited())
        {
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        }

        options = new DisplayImageOptions.Builder().showStubImage(R.drawable.default_profile_picture)
                .showImageForEmptyUri(R.drawable.default_profile_picture)
                .showImageOnFail(R.drawable.default_profile_picture).cacheInMemory(true).cacheOnDisc(false)
                .displayer(new RoundedBitmapDisplayer(20)).build();

        this.hasPositionAndQualification = hasPositionAndQualification;
    }

    private class ViewHolder
    {
        public ImageView image;

        public TextView sureName;

        public TextView name;

        public RatingBar rating;

        public TextView position;

        public CheckBox checkBox;
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

        Player player = (Player) lista.get(position);

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
            holder.checkBox = (CheckBox) view.findViewById(R.id.checkBox1);

            view.setTag(holder);

            holder.checkBox.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    CheckBox cb = (CheckBox) v;
                    Player player = (Player) cb.getTag();
                    if (cb.isChecked())
                    {
                        ids.add(player.getId());
                    }
                    else
                    {
                        ids.remove(player.getId());
                    }
                    player.setSelected(cb.isChecked());
                }
            });

            holder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
            {
                @SuppressLint("NewApi")
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        if (mActionMode == null)
                        {
                            mActionMode = act.startActionMode(new ModeCallback());
                        }
                    }
                    else
                    {
                        if (mActionMode != null)
                        {
                            mActionMode.finish();
                            mActionMode = null;
                        }
                    }
                }
            });
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        holder.name.setText(player.getNome());
        holder.sureName.setText(player.getSobreNome());
        holder.checkBox.setChecked(player.isSelected());

        holder.checkBox.setTag(player);

        if (hasPositionAndQualification)
        {
            /** Qualificacao */
            new PlayerREST(holder.rating, holder.position, context).execute();
        }
        else
        {
            holder.rating.setRating(player.getRating());
        }

        /** Monta a imagem */
        imageLoader.displayImage(player.getPicture(), holder.image, options, animateFirstListener);

        return view;
    }

    public void addItems(List<Player> newItems)
    {
        if (null == newItems || newItems.size() <= 0)
        {
            return;
        }

        if (null == lista)
        {
            lista = new ArrayList<Player>();
        }

        lista.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener
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

    @SuppressLint("NewApi")
    private class ModeCallback implements ActionMode.Callback
    {

        @SuppressLint("NewApi")
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            menu.add("Teste").setIcon(R.drawable.players_group).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            switch (item.getItemId())
            {
            case 0:
                new Invitation(ids).execute();
            }
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode)
        {
            mActionMode = null;
        }
    }

    private class Invitation extends AsyncTask<Void, String[], String[]>
    {

        private ProgressDialog dialog;

        private List<Long> ids = new ArrayList<Long>();

        public Invitation(List<Long> ids)
        {
            super();
            this.ids = ids;
        }

        @Override
        protected void onPreExecute()
        {
            dialog = new ProgressDialog(context);
            dialog.setMessage("Por favor, aguarde.\nConvocando jogadores...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String[] doInBackground(Void... params)
        {
            StringBuilder sb = new StringBuilder(Constants.URL_GAME_WS + "invite" + Constants.SLASH + gameId
                    + Constants.SLASH + Constants.USER_ID);

            for (Long id : ids)
            {
                sb.append(Constants.SLASH + id);
            }
            String[] resposta = WebServiceClient.get(sb.toString());
            return resposta;
        }

        @Override
        protected void onPostExecute(String[] result)
        {
            dialog.dismiss();
            super.onPostExecute(result);

            if (Constants.WS_STATUS_OK.equals(result[0]))
            {
                android.content.DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        alertDialog.dismiss();
                        act.finish();
                    }
                };

                alertDialog = new AlertUtils(context).getAlertDialog(Constants.WARNING, "Jogadores Convodados!!",
                        positiveButton, null);

                alertDialog.show();
            }
            else
            {
                android.content.DialogInterface.OnClickListener positiveButton = new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        alertDialog.dismiss();
                        Activity act = (Activity) context;
                        act.finish();
                    }
                };

                alertDialog = new AlertUtils(context).getAlertDialog(Constants.WARNING,
                        "Problemas ao convocar jogadores.", positiveButton, null);

                alertDialog.show();
            }
        }
    }
}