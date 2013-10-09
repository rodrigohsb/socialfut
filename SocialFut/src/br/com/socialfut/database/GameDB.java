package br.com.socialfut.database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import br.com.socialfut.persistence.Game;
import br.com.socialfut.persistence.Game.Games;
import br.com.socialfut.util.Constants;

public class GameDB extends SQLiteOpenHelper
{
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS game";

    public static final String TABLE = "GAME";

    private static final String SCRIPT_DATABASE_CREATE = "create table " + TABLE
            + " (_id integer primary key autoincrement," + " title string not null," + " address string not null,"
            + " created_date date not null," + " start_date date not null," + " finish_date date not null);";

    private static final String DATABASE_NAME = "socialFut";

    private static final int DATABASE_VERSION = 1;

    protected SQLiteDatabase db;

    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_PATTERN);

    public GameDB(Context context)
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

    public List<Game> getAllGames()
    {
        Cursor c = getCursor();

        List<Game> games = new ArrayList<Game>();

        if (c.moveToFirst())
        {
            int idxId = c.getColumnIndex(Games._ID);
            int idxTitle = c.getColumnIndex(Games.TITLE);
            int idxAddress = c.getColumnIndex(Games.ADDRESS);
            int idxCreated = c.getColumnIndex(Games.CREATED_DATE);
            int idxStart = c.getColumnIndex(Games.START_DATE);
            int idxFinish = c.getColumnIndex(Games.FINISH_DATE);

            do
            {
                try
                {
                    Game g = new Game();

                    g.setId(c.getLong(idxId));
                    g.setTitle(c.getString(idxTitle));
                    g.setAddress(c.getString(idxAddress));
                    g.setCreatedDate(dateFormat.parse(c.getString(idxCreated)));
                    g.setStartDate(dateFormat.parse(c.getString(idxStart)));
                    g.setFinishDate(dateFormat.parse(c.getString(idxFinish)));

                    games.add(g);
                }
                catch (ParseException e)
                {
                    continue;
                }
            }
            while (c.moveToNext());
        }
        c.close();
        return games;

    }

    public List<Game> getOldGames()
    {

        Cursor c = this.getCursor();

        if (c == null)
        {
            return new ArrayList<Game>();
        }

        List<Game> games = new ArrayList<Game>();

        if (c.moveToFirst())
        {
            int idxFinish = c.getColumnIndex(Games.FINISH_DATE);
            Date today = new Date();

            do
            {
                try
                {
                    Date dateGame = dateFormat.parse(c.getString(idxFinish));
                    if (dateGame.before(today))
                    {
                        Game h = this.fillGame(c, dateGame);
                        games.add(h);
                    }
                }
                catch (ParseException e)
                {
                    continue;
                }
            }
            while (c.moveToNext());
        }
        c.close();
        return games;

    }

    public List<Game> getNewGames()
    {
        Cursor c = this.getCursor();

        if (c == null)
        {
            return new ArrayList<Game>();
        }

        List<Game> games = new ArrayList<Game>();

        if (c.moveToFirst())
        {
            int idxFinish = c.getColumnIndex(Games.FINISH_DATE);
            Date today = new Date();

            do
            {
                try
                {
                    Date dateGame = dateFormat.parse(c.getString(idxFinish));

                    if (dateGame.after(today))
                    {
                        Game h = this.fillGame(c, dateGame);
                        games.add(h);
                    }
                }
                catch (ParseException e)
                {
                    continue;
                }
            }
            while (c.moveToNext());
        }
        c.close();
        return games;

    }

    private Game fillGame(Cursor c, Date date) throws ParseException
    {
        int idxId = c.getColumnIndex(Games._ID);
        int idxTitle = c.getColumnIndex(Games.TITLE);
        int idxAddress = c.getColumnIndex(Games.ADDRESS);
        int idxCreated = c.getColumnIndex(Games.CREATED_DATE);
        int idxStart = c.getColumnIndex(Games.START_DATE);

        Game g = new Game();

        g.setId(c.getLong(idxId));
        g.setTitle(c.getString(idxTitle));
        g.setAddress(c.getString(idxAddress));
        g.setCreatedDate(dateFormat.parse(c.getString(idxCreated)));
        g.setStartDate(dateFormat.parse(c.getString(idxStart)));
        g.setFinishDate(date);

        return g;
    }

    public void saveGame(Game game)
    {
        ContentValues values = new ContentValues();

        values.put(Games.TITLE, game.getTitle());
        values.put(Games.ADDRESS, game.getAddress());
        values.put(Games.CREATED_DATE, dateFormat.format(game.getCreatedDate()));
        values.put(Games.START_DATE, dateFormat.format(game.getStartDate()));
        values.put(Games.FINISH_DATE, dateFormat.format(game.getFinishDate()));
        db.insert(TABLE, null, values);
    }

    public Cursor getCursor()
    {
        try
        {
            return db.query(TABLE, Game.colunas, null, null, null, null, null);
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
