package br.com.socialfut.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import br.com.socialfut.persistence.Chat;
import br.com.socialfut.persistence.Chat.Chats;

public class Repositorio extends SQLiteOpenHelper
{
    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS chat";

    public static final String TABLE = "chat";

    private static final String SCRIPT_DATABASE_CREATE = "create table "
            + TABLE
            + " (_id integer primary key autoincrement, sender long not null, content text not null, date timestamp default current_timestamp)";

    private static final String DATABASE_NAME = "socialFut";

    private static final int DATABASE_VERSION = 1;

    private static final String NOME_TABELA = "chat";

    protected SQLiteDatabase db;

    public Repositorio(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public List<Chat> searchHistory(long id)
    {
        Cursor c = getCursor();

        List<Chat> chatList = new ArrayList<Chat>();

        if (c.moveToFirst())
        {
            int idxId = c.getColumnIndex(Chats._ID);
            int idxSender = c.getColumnIndex(Chats.SENDER);
            int idxCont = c.getColumnIndex(Chats.CONTENT);
            // int idxTime = c.getColumnIndex(Chats.DATE);
            do
            {
                Chat chat = new Chat();
                chat.setId(c.getLong(idxId));
                chat.setSender(c.getLong(idxSender));
                chat.setContent(c.getString(idxCont));
                // chat.setTime("");

                chatList.add(chat);
            }
            while (c.moveToNext());
        }
        c.close();
        return chatList;

    }

    public void save(Chat chat)
    {
        ContentValues values = new ContentValues();

        values.put(Chats.SENDER, chat.getSender());
        values.put(Chats.DATE, chat.getTime().toString());
        values.put(Chats.CONTENT, chat.getContent());
        System.out.println("[save] Salvando Sender [" + chat.getSender() + "], content[" + chat.getContent() + "].");
        db.insert(NOME_TABELA, null, values);
    }

    public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy)
    {
        Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
        return c;
    }

    public Cursor getCursor()
    {
        try
        {
            return db.query(NOME_TABELA, Chat.colunas, null, null, null, null, null);
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
