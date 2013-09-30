package br.com.socialfut.util;

import android.content.Context;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
