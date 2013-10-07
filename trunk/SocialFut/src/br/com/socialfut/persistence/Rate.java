package br.com.socialfut.persistence;

import java.io.Serializable;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import br.com.socialfut.persistence.Chat.Chats;

public class Rate implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static String[] colunas = new String[] { Rates._ID, Rates.GAME_ID, Rates.VALUE, Rates.QNT_RATES };

    public static final String AUTHORITY = "br.com.socialfut.android.provider.rate";

    private long id;

    private long gameId;

    private int qntRates;

    private double value;

    private double rating;

    public Rate(long gameId, int qntRates, double value)
    {
        super();
        this.gameId = gameId;
        this.qntRates = qntRates;
        this.value = value;
        this.rating = this.value / this.qntRates;
    }

    public Rate()
    {
        super();
    }

    public Rate(int qntRates)
    {
        super();
        this.qntRates = qntRates;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public int getQntRates()
    {
        return qntRates;
    }

    public void setQntRates(int qntRates)
    {
        this.qntRates = qntRates;
    }

    public double getValue()
    {
        return value;
    }

    public void setValue(double value)
    {
        this.value = value;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }

    @Override
    public String toString()
    {
        return super.toString();
    }

    public static final class Rates implements BaseColumns
    {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/rate");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.rate";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.rate";

        public static final String DEFAULT_SORT_ORDER = "id ACS";

        public static final String GAME_ID = "game_id";

        public static final String QNT_RATES = "qntRates";

        public static final String VALUE = "value";

        public static Uri getUri(long id)
        {
            Uri uriChat = ContentUris.withAppendedId(Chats.CONTENT_URI, id);
            return uriChat;
        }

    }

}
