package br.com.socialfut.activities;

import java.util.Arrays;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.drawer.SherlockActionBarDrawerToggle;
import br.com.socialfut.util.Constants;
import br.com.socialfut.util.FacebookUtils;
import br.com.socialfut.util.Position;
import br.com.socialfut.webservices.PlayerREST;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente proposito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 05/09/2013
 * 
 */
public class MainFragment extends SherlockFragment
{

    private DrawerLayout mDrawerLayout;

    private ScrollView scrollView;

    private ActionBarHelper mActionBar;

    private SherlockActionBarDrawerToggle mDrawerToggle;

    private UiLifecycleHelper uiHelper;

    private ImageView img;

    private TextView name;

    private TextView sureName;

    private RatingBar ratingUser;

    private TextView position;

    private LoginButton loginButton;

    private Context ctx;

    private boolean alreadyGetProfile = false;

    public static Fragment newInstance()
    {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);

        ctx = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main_layout, container, false);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DemoDrawerListener());
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        scrollView = (ScrollView) view.findViewById(R.id.left_drawer);

        /** Avatar */
        img = (ImageView) scrollView.findViewById(R.id.icon_drawer);

        /** Nome */
        name = (TextView) scrollView.findViewById(R.id.name_drawer);

        /** Sobrenome */
        sureName = (TextView) scrollView.findViewById(R.id.sureName_drawer);

        /** Rate */
        ratingUser = (RatingBar) scrollView.findViewById(R.id.ratingUser);

        position = (TextView) scrollView.findViewById(R.id.position_drawer);

        loginButton = (LoginButton) scrollView.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));

        /** Historico */
        view.findViewById(R.id.myHistory).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getSherlockActivity(), MyHistoryActivity.class));
            }
        });

        /** Partidas Futuras */
        view.findViewById(R.id.future).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getSherlockActivity(), MyFutureActivity.class));
            }
        });

        /** Nova Partida */
        view.findViewById(R.id.creatGame).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getSherlockActivity(), NewGameActivity.class));
            }
        });

        /** Lista de Jogadores */
        view.findViewById(R.id.playerList).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getSherlockActivity(), PlayerListActivity.class));
            }
        });

        mActionBar = createActionBarHelper();
        mActionBar.init();

        mDrawerToggle = new SherlockActionBarDrawerToggle(this.getActivity(), mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        if (sharedPrefs.getBoolean("first_time", true))
        {
            choosePosition();
        }

        return view;
    }

    private void choosePosition()
    {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);
        dialog.setTitle("Por favor, selecione sua posicao");

        LayoutInflater li = (LayoutInflater) getSherlockActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = li.inflate(R.layout.layout_choose_position, null);
        dialog.setView(dialogView);

        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner1);

        dialog.setPositiveButton("Escolher", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                position.setText(Position.values()[spinner.getSelectedItemPosition()].toString().replace("_", " "));
                Constants.POSITION_ID = spinner.getSelectedItemPosition();
            }
        });
        dialog.show();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx, R.array.spinner_itens,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater = ((SherlockFragmentActivity) getActivity()).getSupportMenuInflater();
        inflater.inflate(R.menu.menu_inicio, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        switch (item.getItemId())
        {

        case R.id.chat:
            startActivity(new Intent(getSherlockActivity(), ChatListActivity.class));
            break;

        case R.id.dice:
            startActivity(new Intent(getSherlockActivity(), PlayerListActivity.class));
            break;

        case R.id.cronometro:
            startActivity(new Intent(getSherlockActivity(), ChronometerActivity.class));
            break;

        case R.id.subtitle:
            startActivity(new Intent(getSherlockActivity(), LegendaActivity.class));
            break;

        case R.id.exit:
            getSherlockActivity().finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume()
    {
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed()) && !alreadyGetProfile)
        {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState state, Exception exception)
    {
        if (state.isOpened())
        {
            loginButton.setVisibility(View.GONE);

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
            if (sharedPrefs.getBoolean("first_time", true))
            {
                alreadyGetProfile = true;
                sharedPrefs.edit().putBoolean("first_time", false).commit();
                FacebookUtils.getProfile(session, name, sureName, img, ctx);
                new PlayerREST(position).execute();
            }
            else
            {
                alreadyGetProfile = true;
                FacebookUtils.getProfile(session, name, sureName, img, ratingUser, position, ctx);
                new PlayerREST().execute();
            }
        }
        else if (state.isClosed())
        {
            getDataFromPreference();
        }
        else if (session.getState() == SessionState.OPENING)
        {
            alreadyGetProfile = true;
        }

    }

    private Session.StatusCallback callback = new Session.StatusCallback()
    {
        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            onSessionStateChange(session, state, exception);
        }
    };

    private class DemoDrawerListener implements DrawerLayout.DrawerListener
    {
        @Override
        public void onDrawerOpened(View drawerView)
        {
            mDrawerToggle.onDrawerOpened(drawerView);
            mActionBar.onDrawerOpened();
        }

        @Override
        public void onDrawerClosed(View drawerView)
        {
            mDrawerToggle.onDrawerClosed(drawerView);
            mActionBar.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset)
        {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState)
        {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }

    private ActionBarHelper createActionBarHelper()
    {
        return new ActionBarHelper();
    }

    private class ActionBarHelper
    {
        private final ActionBar mActionBar;

        private CharSequence mTitle;

        private ActionBarHelper()
        {
            mActionBar = ((SherlockFragmentActivity) getActivity()).getSupportActionBar();
        }

        public void init()
        {
            br.com.socialfut.util.ActionBar.updateActionBar(mActionBar);
            mTitle = getActivity().getTitle();
        }

        /**
         * When the drawer is closed we restore the action bar state reflecting
         * the specific contents in view.
         */
        public void onDrawerClosed()
        {
            mActionBar.setTitle(mTitle);
        }

        /**
         * When the drawer is open we set the action bar to a generic title. The
         * action bar should only contain data relevant at the top level of the
         * nav hierarchy represented by the drawer, as the rest of your content
         * will be dimmed down and non-interactive.
         */
        public void onDrawerOpened()
        {
            mActionBar.setTitle("Perfil");
        }
    }

    private void getDataFromPreference()
    {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        String nome = sharedPrefs.getString("name", null);
        String previouslyEncodedImage = sharedPrefs.getString("image", null);

        if (previouslyEncodedImage != null)
        {
            byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            img.setImageBitmap(bitmap);
        }
        else
        {
            Drawable d = getResources().getDrawable(R.drawable.ic_stub);
            img.setImageDrawable(d);
        }

        if (null != nome)
        {
            name.setText(nome);
            sureName.setText(nome);
        }
        else
        {
            name.setText("");
            sureName.setText("");
        }

    }
}
