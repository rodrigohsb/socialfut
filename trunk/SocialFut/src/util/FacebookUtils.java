package util;

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

    public static void getName(Session sessao, TextView txtName, Context ctx)
    {
        if (sessao != null && (sessao.getState().isOpened()))
        {
            facebookName = new FacebookName(sessao, txtName, ctx);
            facebookName.execute();
        }
    }

    public static void getSureName(Session sessao, TextView txtName, Context ctx)
    {
        if (sessao != null && (sessao.getState().isOpened()))
        {
            facebookName = new FacebookName(sessao, txtName, ctx);
            facebookName.execute();
        }
    }

    public static void getPicture(Session sessao, ImageView imgAvatar)
    {
        if (sessao != null && (sessao.getState().isOpened()))
        {
            facebookPicture = new FacebookPicture(sessao, imgAvatar);
            facebookPicture.execute();
        }
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
