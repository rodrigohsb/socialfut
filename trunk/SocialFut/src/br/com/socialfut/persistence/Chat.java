package br.com.socialfut.persistence;

import java.io.Serializable;
import java.sql.Timestamp;

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

    public static String[] colunas = new String[] { Chats._ID, Chats.SENDER, Chats.RECEIVER, Chats.CONTENT };

    public static final String AUTHORITY = "br.com.socialfut.android.provider.chat";

    private long id;

    private long sender;

    private long receiver;

    private String content;

    private Timestamp time;

    public Chat()
    {
        super();
    }

    public Chat(long id, long sender, long receiver, String content, Timestamp time)
    {
        super();
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
    }

    public Chat(long sender, long receiver, String content, Timestamp time)
    {
        super();
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
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

    public Timestamp getTime()
    {
        return time;
    }

    public void setTime(Timestamp time)
    {
        this.time = time;
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

        public static final String DATE = "date";

        public static Uri getUri(long id)
        {
            Uri uriChat = ContentUris.withAppendedId(Chats.CONTENT_URI, id);
            return uriChat;
        }

    }

}
