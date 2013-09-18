package activities;

import java.util.Arrays;

import teste.SherlockActionBarDrawerToggle;
import util.FacebookUtils;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import br.com.futcefet.R;

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

    private static final String TAG = "MainFragment";

    private DrawerLayout mDrawerLayout;

    private ScrollView scrollView;

    private ActionBarHelper mActionBar;

    private SherlockActionBarDrawerToggle mDrawerToggle;

    private UiLifecycleHelper uiHelper;

    private Session session;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main_layout, container, false);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(new DemoDrawerListener());
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        scrollView = (ScrollView) view.findViewById(R.id.left_drawer);
        ImageView img = (ImageView) scrollView.findViewById(R.id.icon_drawer);
        TextView name = (TextView) scrollView.findViewById(R.id.name_drawer);
        TextView sureName = (TextView) scrollView.findViewById(R.id.sureName_drawer);
        LoginButton loginButton = (LoginButton) scrollView.findViewById(R.id.login_button);
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

        // Bitmap bmp = BitmapFactory.decodeResource(getResources(),
        // R.drawable.rodrigo);
        // img.setImageBitmap(ImageHelper.getRoundedCornerBitmap(bmp));
        // name.setText("Rodrigo");

        FacebookUtils.getPicture(session, img);
        FacebookUtils.getName(session, name);

        sureName.setText("Bacellar");

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

        case R.id.dice:
            startActivity(new Intent(getSherlockActivity(), PlayerListForSort.class));
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
            if (session.isOpened())
            {
                Log.i(TAG, "Logged in...");
            }
            else if (session.isClosed())
            {
                Log.i(TAG, "Logged out...");
            }
        }

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

    private Session.StatusCallback callback = new Session.StatusCallback()
    {
        @Override
        public void call(Session session, SessionState state, Exception exception)
        {
            if (state.isOpened())
            {
                Log.i(TAG, "Logged in...");
            }
            else if (state.isClosed())
            {
                Log.i(TAG, "Logged out...");
            }
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
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);
            mTitle = getActivity().getTitle();
            mActionBar.setIcon(R.drawable.icone);
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#008000")));
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
