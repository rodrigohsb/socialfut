package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper
{

    private static final String CATEGORIA = "Social_Fut";

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

        Log.i(CATEGORIA, "Criando banco em sql...");

        int qntScripts = scriptSQLCreate.length;

        for (int i = 0; i < qntScripts; i++)
        {
            String sql = scriptSQLCreate[i];
            Log.i(CATEGORIA, sql);
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.w(CATEGORIA, "Atualizando a versao" + oldVersion + "para" + newVersion
                + ". Todos os registros serão deletados.");
        Log.i(CATEGORIA, scriptSQLDelete);
        db.execSQL(scriptSQLDelete);
        onCreate(db);
    }
}
