package br.com.socialfut.activities;

import java.util.Arrays;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import br.com.socialfut.R;
import br.com.socialfut.drawer.SherlockActionBarDrawerToggle;
import br.com.socialfut.util.FacebookUtils;

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
 * TODO Explicar detalhadamente propósito da classe.
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

    private LoginButton loginButton;

    private Context ctx;

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
        img = (ImageView) scrollView.findViewById(R.id.icon_drawer);
        name = (TextView) scrollView.findViewById(R.id.name_drawer);
        sureName = (TextView) scrollView.findViewById(R.id.sureName_drawer);

        // TODO Somente mostrar o botao do Facebook se o usuario nao esta
        // logado.

        loginButton = (LoginButton) scrollView.findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("user_likes", "user_status"));

        view.findViewById(R.id.playerList).setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == R.id.playerList)
                {
                    startActivity(new Intent(getSherlockActivity(), PlayerListActivity.class));
                }
            }
        });

        mActionBar = createActionBarHelper();
        mActionBar.init();

        mDrawerToggle = new SherlockActionBarDrawerToggle(this.getActivity(), mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        return view;
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
        if (session != null && (session.isOpened() || session.isClosed()))
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
            FacebookUtils.getPicture(session, img, ctx);
            FacebookUtils.getName(session, name, sureName, ctx);
        }
        else if (state.isClosed())
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

}
