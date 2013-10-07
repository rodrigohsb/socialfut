package br.com.socialfut.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.persistence.Rate;
import br.com.socialfut.persistence.Rate.Rates;

public class RateDB extends SQLiteOpenHelper
{
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS RATE";

    public static final String TABLE = "RATE";

    private static final String SCRIPT_DATABASE_CREATE = "create table " + TABLE + " (_id integer primary key autoincrement," + " game_id integer not null," + " qntRates int not null," + " value double not null);";

    private static final String DATABASE_NAME = "socialFut";

    private static final int DATABASE_VERSION = 1;

    protected SQLiteDatabase db;

    public RateDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SCRIPT_DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SCRIPT_DATABASE_DELETE);
        onCreate(db);
    }

    public List<Rate> getAllRates()
    {
        Cursor c = getCursor();

        List<Rate> rates = new ArrayList<Rate>();

        if (c.moveToFirst())
        {
            int idxId = c.getColumnIndex(Rates._ID);
            int idxgameId = c.getColumnIndex(Rates.GAME_ID);
            int idxValue = c.getColumnIndex(Rates.VALUE);
            int idxQntRates = c.getColumnIndex(Rates.QNT_RATES);
            do
            {
                Rate r = new Rate();

                r.setId(c.getLong(idxId));

                r.setGameId(c.getLong(idxgameId));

                int qntRates = c.getInt(idxQntRates);
                r.setQntRates(qntRates);

                double value = c.getDouble(idxValue);
                r.setValue(value);

                double rating = (value / qntRates);

                r.setRating(rating);

                rates.add(r);
            }
            while (c.moveToNext());
        }

        c.close();
        return rates;
    }

    public void saveRate(Rate rate)
    {
        ContentValues values = new ContentValues();

        values.put(Rates.GAME_ID, rate.getGameId());
        values.put(Rates.VALUE, rate.getValue());
        values.put(Rates.QNT_RATES, rate.getQntRates());
        db.insert(TABLE, null, values);
    }

    public void updateRate(long gameId, int value)
    {
        ContentValues values = new ContentValues();

        values.put(Rates.GAME_ID, gameId);
        values.put(Rates.VALUE, value);

        /** Obtem a quantidada de rates atual para somar 1 */
        int qntRates = getQntRatesByGameId(gameId);
        values.put(Rates.QNT_RATES, ++qntRates);

        db.insert(TABLE, null, values);
    }

    public Rate getRateById(long id)
    {
        try
        {
            Cursor c = db.query(TABLE, Rate.colunas, "id = " + id, null, null, null, null);

            int idxId = c.getColumnIndex(Rates._ID);
            int idxgameId = c.getColumnIndex(Rates.GAME_ID);
            int idxValue = c.getColumnIndex(Rates.VALUE);
            int idxQntRates = c.getColumnIndex(Rates.QNT_RATES);

            if (c.moveToNext())
            {
                Rate r = new Rate();

                r.setId(c.getLong(idxId));

                r.setGameId(c.getLong(idxgameId));

                int qntRates = c.getInt(idxQntRates);
                r.setQntRates(qntRates);

                double value = c.getDouble(idxValue);
                r.setValue(value);

                double rating = (value / qntRates);

                r.setRating(rating);
                return r;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public Rate getRateByGame(Game g)
    {
        try
        {
            Cursor c = db.query(TABLE, Rate.colunas, "game_id = " + g.getId(), null, null, null, null);

            int idxId = c.getColumnIndex(Rates._ID);
            int idxgameId = c.getColumnIndex(Rates.GAME_ID);
            int idxValue = c.getColumnIndex(Rates.VALUE);
            int idxQntRates = c.getColumnIndex(Rates.QNT_RATES);

            if (c.moveToNext())
            {
                Rate r = new Rate();

                r.setId(c.getLong(idxId));

                r.setGameId(c.getLong(idxgameId));

                int qntRates = c.getInt(idxQntRates);
                r.setQntRates(qntRates);

                double value = c.getDouble(idxValue);
                r.setValue(value);

                double rating = (value / qntRates);

                r.setRating(rating);
                return r;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public double getRateByGameId(Game g)
    {
        Rate r = getRateByGame(g);

        int qntRates = r.getQntRates();

        double value = r.getValue();

        return value / qntRates;
    }

    public int getQntRatesByGameId(long id)
    {
        Rate r = getRateById(id);

        return r.getQntRates();
    }

    public Cursor getCursor()
    {
        try
        {
            return db.query(TABLE, Rate.colunas, null, null, null, null, null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

    public void fechar()
    {
        if (db != null)
        {
            db.close();
        }
    }

    public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy)
    {
        Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
        return c;
    }
}
