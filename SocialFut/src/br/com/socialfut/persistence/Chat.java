package br.com.socialfut.persistence;

import java.io.Serializable;
import java.util.Date;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 24/09/2013
 * 
 */
public class Chat implements Serializable
{

    private static final long serialVersionUID = 1L;

    public static String[] colunas = new String[] { Chats._ID, Chats.SENDER, Chats.RECEIVER, Chats.CONTENT, Chats.DATE };

    public static final String AUTHORITY = "br.com.socialfut.android.provider.chat";

    private long id;

    private long sender;

    private long receiver;

    private String content;

    private Date date;

    public Chat()
    {
        super();
    }

    public Chat(long sender, long receiver, String content)
    {
        super();
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.date = new Date();
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getSender()
    {
        return sender;
    }

    public void setSender(long sender)
    {
        this.sender = sender;
    }

    public long getReceiver()
    {
        return receiver;
    }

    public void setReceiver(long receiver)
    {
        this.receiver = receiver;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    @Override
    public String toString()
    {
        return content;
    }

    public static final class Chats implements BaseColumns
    {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/chat");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.chat";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.chat";

        public static final String DEFAULT_SORT_ORDER = "id ACS";

        public static final String SENDER = "sender";

        public static final String RECEIVER = "receiver";

        public static final String CONTENT = "content";

        public static final String DATE = "created_date";

        public static Uri getUri(long id)
        {
            Uri uriChat = ContentUris.withAppendedId(Chats.CONTENT_URI, id);
            return uriChat;
        }

    }

}
