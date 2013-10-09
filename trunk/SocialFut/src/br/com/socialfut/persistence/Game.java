package br.com.socialfut.persistence;

import java.io.Serializable;
import java.util.Date;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import br.com.socialfut.persistence.Chat.Chats;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 03/10/2013
 * 
 */
public class Game implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static String[] colunas = new String[] { Games._ID, Games.TITLE, Games.ADDRESS, Games.CREATED_DATE,
            Games.START_DATE, Games.FINISH_DATE };

    public static final String AUTHORITY = "br.com.socialfut.android.provider.histories";

    private long id;

    private String title;

    private String address;

    private Date createdDate;

    private Date startDate;

    private Date finishDate;

    public Game()
    {
        super();
    }

    public Game(String title, String address, Date createdDate, Date startDate, Date finishDate)
    {
        super();
        this.title = title;
        this.address = address;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.finishDate = finishDate;

    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(Date finishDate)
    {
        this.finishDate = finishDate;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    public static final class Games implements BaseColumns
    {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/games");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.games";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.games";

        public static final String DEFAULT_SORT_ORDER = "id ACS";

        public static final String TITLE = "title";

        // public static final String PICTURE = "picture";

        public static final String ADDRESS = "address";

        public static final String CREATED_DATE = "created_date";

        public static final String START_DATE = "start_date";

        public static final String FINISH_DATE = "finish_date";

        public static Uri getUri(long id)
        {
            Uri uriChat = ContentUris.withAppendedId(Chats.CONTENT_URI, id);
            return uriChat;
        }

    }

}
