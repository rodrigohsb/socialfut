package br.com.socialfut.persistence;

import java.io.Serializable;

public class Player implements Serializable
{

    private static final long serialVersionUID = 1L;

    private long id;

    private String nome;

    private String sobreNome;

    private String position;

    private float rating;

    private String picture;

    boolean selected = false;

    public Player()
    {
    }

    public Player(long id, String nome, String sobreNome, String picture)
    {
        super();
        this.id = id;
        this.nome = nome;
        this.sobreNome = sobreNome;
        this.picture = picture;
    }

    public Player(long id, String position, float rating)
    {
        super();
        this.id = id;
        this.position = position;
        this.rating = rating;
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

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    @Override
    public String toString()
    {
        return nome + " " + sobreNome;
    }
}
