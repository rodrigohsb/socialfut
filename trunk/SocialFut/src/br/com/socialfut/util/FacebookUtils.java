package br.com.socialfut.util;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.socialfut.webservices.WebService;

import com.facebook.Session;

public class FacebookUtils
{

    private static FacebookName facebookName;

    private static FacebookPicture facebookPicture;

    private static FacebookPost facebookPost;

    public static void getProfile(Session sessao, TextView txtName, TextView txtSureName, ImageView imgAvatar,
            Context ctx)
    {
        if (sessao != null && (sessao.getState().isOpened()))
        {
            getName(sessao, txtName, txtSureName, ctx);
            getPicture(sessao, imgAvatar, ctx);
        }
        if (Constants.USER_ID != 0 && Constants.DEVICE_REGISTRATION_ID != null)
        {
            WebService.sendRegistration();
        }
    }

    public static void getName(Session sessao, TextView txtName, TextView txtSureName, Context ctx)
    {
        facebookName = new FacebookName(sessao, txtName, txtSureName, ctx);
        facebookName.execute();
    }

    public static void getPicture(Session sessao, ImageView imgAvatar, Context ctx)
    {
        facebookPicture = new FacebookPicture(sessao, imgAvatar, ctx);
        facebookPicture.execute();
    }

    public static void setPostMsg(Session sessao, EditText edtPostMsg)
    {
        if (sessao != null && (sessao.getState().isOpened()))
        {
            facebookPost = new FacebookPost(sessao, edtPostMsg);
            facebookPost.execute();
        }
    }
}
