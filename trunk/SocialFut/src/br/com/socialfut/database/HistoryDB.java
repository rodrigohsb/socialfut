package br.com.socialfut.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import br.com.socialfut.persistence.History;
import br.com.socialfut.persistence.History.Histories;
import br.com.socialfut.util.Constants;

public class HistoryDB extends SQLiteOpenHelper
{
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS history";

    public static final String TABLE = "HISTORY";

    private static final String SCRIPT_DATABASE_CREATE = "create table " + TABLE
            + " (_id integer primary key autoincrement," + " title string not null," + " picture string not null,"
            + " address string not null," + " created_date date not null," + " start_date date not null,"
            + " finish_date date not null)";

    private static final String DATABASE_NAME = "socialFut";

    private static final int DATABASE_VERSION = 1;

    protected SQLiteDatabase db;

    public HistoryDB(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    /**
     * 
     * Obtem o historico das partidas.
     * 
     * @return
     */
    public List<History> getHistory()
    {
        Cursor c = getCursor();

        List<History> histories = new ArrayList<History>();

        if (c.moveToFirst())
        {
            int idxId = c.getColumnIndex(Histories._ID);
            int idxTitle = c.getColumnIndex(Histories.TITLE);
            int idxPicture = c.getColumnIndex(Histories.PICTURE);
            int idxAddress = c.getColumnIndex(Histories.ADDRESS);
            int idxCreated = c.getColumnIndex(Histories.CREATED_DATE);
            int idxStart = c.getColumnIndex(Histories.START_DATE);
            int idxFinish = c.getColumnIndex(Histories.FINISH_DATE);
            SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);

            do
            {
                try
                {
                    History h = new History();

                    h.setId(c.getLong(idxId));
                    h.setTitle(c.getString(idxTitle));
                    h.setPicture(c.getString(idxPicture));
                    h.setAddress(c.getString(idxAddress));
                    h.setCreatedDate(dateFormat.parse(c.getString(idxCreated)));
                    h.setStartDate(dateFormat.parse(c.getString(idxStart)));
                    h.setFinishDate(dateFormat.parse(c.getString(idxFinish)));

                    histories.add(h);
                }
                catch (ParseException e)
                {
                    continue;
                }
            }
            while (c.moveToNext());
        }
        c.close();
        return histories;

    }

    /**
     * 
     * Salva o historico das partidas.
     * 
     * @param history
     */
    public void saveHistory(History history)
    {
        ContentValues values = new ContentValues();

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);

        values.put(Histories.TITLE, history.getTitle());
        values.put(Histories.ADDRESS, history.getAddress());
        values.put(Histories.PICTURE, history.getPicture());
        values.put(Histories.CREATED_DATE, dateFormat.format(history.getCreatedDate()));
        values.put(Histories.START_DATE, dateFormat.format(history.getStartDate()));
        values.put(Histories.FINISH_DATE, dateFormat.format(history.getFinishDate()));
        db.insert(TABLE, null, values);
    }

    /**
     * 
     * Obtem o historico por usuario.
     * 
     * @param userId
     * @return
     */
    public Cursor getCursor()
    {
        try
        {
            return db.query(TABLE, History.colunas, null, null, null, null, null);
        }
        catch (Exception e)
        {
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
}
