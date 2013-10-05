package br.com.socialfut.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{

    private String[] scriptSQLCreate;

    private String scriptSQLDelete;

    public SQLiteHelper(Context context, String nomeBanco, int versaoBanco, String[] scriptSQLCreate,
            String scriptSQLDetete)
    {
        super(context, nomeBanco, null, versaoBanco);

        this.scriptSQLCreate = scriptSQLCreate;
        this.scriptSQLDelete = scriptSQLDetete;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        int qntScripts = scriptSQLCreate.length;

        for (int i = 0; i < qntScripts; i++)
        {
            String sql = scriptSQLCreate[i];
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(scriptSQLDelete);
        onCreate(db);
    }
}
