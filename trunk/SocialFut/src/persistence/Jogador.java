package persistence;

import java.io.Serializable;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class Jogador implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static String[] colunas = new String[] { Jogadores._ID, Jogadores.NOME, Jogadores.SOBRENOME,
            Jogadores.POSICAO, Jogadores.RATING, Jogadores.TELEFONE, Jogadores.PICTURE };

    public static final String AUTHORITY = "br.com.socialfut.android.provider.carro";

    private long id;

    private String nome;

    private String sobreNome;

    private String position;

    private float rating;

    private String tel;

    private String picture;

    public Jogador()
    {
    }

    public Jogador(long id, String nome, String sobreNome, String picture)
    {
        super();
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.picture = picture;
    }

    public Jogador(String nome, String sobreNome, String posicao, float rating, String tel)
    {
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.position = posicao;
        this.rating = rating;
        this.tel = tel;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getTel()
    {
        return tel;
    }

    public void setTel(String tel)
    {
        this.tel = tel;
    }

    public String getSobreNome()
    {
        return sobreNome;
    }

    public void setSobreNome(String sobreNome)
    {
        this.sobreNome = sobreNome;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    @Override
    public String toString()
    {
        return nome + " " + sobreNome;
    }

    public static final class Jogadores implements BaseColumns
    {

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/jogadores");

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.jogadores";

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.jogadores";

        public static final String DEFAULT_SORT_ORDER = "id ACS";

        public static final String NOME = "nome";

        public static final String SOBRENOME = "sobrenome";

        public static final String POSICAO = "posicao";

        public static final String RATING = "rating";

        public static final String TELEFONE = "telefone";

        public static final String PICTURE = "picture";

        public static Uri getUri(long id)
        {
            Uri uriCarro = ContentUris.withAppendedId(Jogadores.CONTENT_URI, id);
            return uriCarro;
        }

    }

}
