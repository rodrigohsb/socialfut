package br.com.socialfut.database;

import java.util.ArrayList;
import java.util.List;

import br.com.socialfut.persistence.Jogador;
import br.com.socialfut.persistence.Jogador.Jogadores;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class Repositorio
{

    private static final String CATEGORIA = "SocialFut";

    private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS chat";

    private static final String[] SCRIPT_DATABASE_CREATE = new String[] {
            "create table chat(_facebook_id integer primary key," + "content text not null" + "date timestamp not null default current_timestamp);",
            "insert into player(nome, sobrenome, posicao,rating,telefone,picture) values('Rodrigo','Bacellar','Meio-Campo','5.0','9625-6313','https://graph.facebook.com/rodrigo.haus/picture');",
            "insert into player(nome, sobrenome, posicao,rating,telefone,picture) values('Marcio','Telles','Goleiro','3.5','9999-6666','https://graph.facebook.com/marcio.telles.10/picture');",
            "insert into player(nome, sobrenome, posicao,rating,telefone,picture) values('Rafael','Cardoso','Meio-Campo','3.0','9845-5155','https://graph.facebook.com/rafael.cardosotelles/picture');" };

    private static final String NOME_BANCO = "socialFut";

    private static final int VERSAO_BANCO = 1;

    private static final String NOME_TABELA = "chat";

    private SQLiteHelper dbhelper;

    protected SQLiteDatabase db;

    public Repositorio(Context ctx)
    {
        dbhelper = new SQLiteHelper(ctx, NOME_BANCO, VERSAO_BANCO, SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);

        db = dbhelper.getWritableDatabase();
    }

    public long salvar(Jogador jogador)
    {
        long id = jogador.getId();
        if (id != 0)
        {
            atualizar(jogador);
        }
        else
        {
            id = inserir(jogador);
        }
        return id;
    }

    public long inserir(Jogador Jogador)
    {
        ContentValues values = new ContentValues();

        values.put(Jogadores.NOME, Jogador.getNome());
        values.put(Jogadores.SOBRENOME, Jogador.getSobreNome());
        values.put(Jogadores.POSICAO, Jogador.getPosition());
        values.put(Jogadores.RATING, Jogador.getRating());
        values.put(Jogadores.TELEFONE, Jogador.getTel());
        values.put(Jogadores.PICTURE, Jogador.getPicture());

        long id = inserir(values);

        return id;
    }

    private long inserir(ContentValues values)
    {
        long id = db.insert(NOME_TABELA, "", values);
        return id;
    }

    public int atualizar(Jogador Jogador)
    {
        ContentValues values = new ContentValues();

        values.put(Jogadores.NOME, Jogador.getNome());
        values.put(Jogadores.SOBRENOME, Jogador.getSobreNome());
        values.put(Jogadores.POSICAO, Jogador.getPosition());
        values.put(Jogadores.RATING, Jogador.getRating());
        values.put(Jogadores.TELEFONE, Jogador.getTel());
        values.put(Jogadores.PICTURE, Jogador.getPicture());

        String id = String.valueOf(Jogador.getId());

        String where = Jogadores._ID + "=?";

        String[] whereArgs = new String[] { id };

        int count = atualizar(values, where, whereArgs);

        return count;

    }

    private int atualizar(ContentValues values, String where, String[] whereArgs)
    {
        int count = db.update(NOME_TABELA, values, where, whereArgs);
        Log.i(CATEGORIA, "Atualizou [" + count + "] registros.");
        return count;
    }

    public int deletar(long id)
    {
        String where = Jogadores._ID + "=?";

        String _id = String.valueOf(id);

        String[] whereArgs = new String[] { _id };

        int count = deletar(where, whereArgs);

        return count;

    }

    private int deletar(String where, String[] whereArgs)
    {
        int count = db.delete(NOME_TABELA, where, whereArgs);
        Log.i(CATEGORIA, "Deletou [" + count + "] registros.");
        return count;

    }

    public Jogador buscarJogador(long id)
    {
        Cursor c = db.query(true, NOME_TABELA, Jogador.colunas, Jogadores._ID + "=" + id, null, null, null, null, null);

        if (c.getCount() > 0)
        {
            c.moveToFirst();

            Jogador jogador = new Jogador();

            jogador.setId(c.getLong(0));
            jogador.setNome(c.getString(1));
            jogador.setSobreNome(c.getString(2));
            jogador.setPosition(c.getString(3));
            jogador.setRating(c.getFloat(4));
            jogador.setTel(c.getString(5));
            jogador.setPicture(c.getString(6));
            return jogador;
        }
        return null;
    }

    public Cursor getCursor()
    {
        try
        {
            return db.query(NOME_TABELA, Jogador.colunas, null, null, null, null, null);
        }
        catch (Exception e)
        {
            Log.e(CATEGORIA, "Erro ao buscar os jogadores: " + e.toString());
            return null;
        }

    }

    public List<Jogador> listarJogadores()
    {
        Cursor c = getCursor();

        List<Jogador> jogadores = new ArrayList<Jogador>();

        if (c.moveToFirst())
        {
            int idxId = c.getColumnIndex(Jogadores._ID);
            int idxNome = c.getColumnIndex(Jogadores.NOME);
            int idxSobre = c.getColumnIndex(Jogadores.SOBRENOME);
            int idxPos = c.getColumnIndex(Jogadores.POSICAO);
            int idxRat = c.getColumnIndex(Jogadores.RATING);
            int idxTel = c.getColumnIndex(Jogadores.TELEFONE);
            int idxPic = c.getColumnIndex(Jogadores.PICTURE);

            do
            {
                Jogador jogador = new Jogador();
                jogador.setId(c.getLong(idxId));
                jogador.setNome(c.getString(idxNome));
                jogador.setSobreNome(c.getString(idxSobre));
                jogador.setPosition(c.getString(idxPos));
                jogador.setRating(c.getFloat(idxRat));
                jogador.setTel(c.getString(idxTel));
                jogador.setPicture(c.getString(idxPic));

                jogadores.add(jogador);
            }
            while (c.moveToNext());
        }
        return jogadores;
    }

    public Jogador buscarJogadorPeloNome(String nome)
    {
        Jogador jogador = null;

        try
        {
            Cursor c = db.query(NOME_TABELA, Jogador.colunas, Jogadores.NOME + "='" + "nome" + "'", null, null, null,
                    null);

            if (c.moveToFirst())
            {
                jogador = new Jogador();

                jogador.setId(c.getLong(0));
                jogador.setNome(c.getString(1));
                jogador.setSobreNome(c.getString(2));
                jogador.setPosition(c.getString(4));
                jogador.setRating(c.getFloat(4));
                jogador.setTel(c.getString(5));
                jogador.setPicture(c.getString(6));
            }

        }
        catch (SQLException e)
        {
            Log.e(CATEGORIA, "Erro ao buscar o caro pelo nome: " + e.toString());
            return null;
        }

        return jogador;
    }

    public Cursor query(SQLiteQueryBuilder queryBuilder, String[] projection, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy)
    {
        Cursor c = queryBuilder.query(this.db, projection, selection, selectionArgs, groupBy, having, orderBy);
        return c;
    }

    public void fechar()
    {
        if (db != null)
        {
            db.close();
        }
        if (dbhelper != null)
        {
            dbhelper.close();
        }
    }

}
