package br.com.socialfut.persistence;

import java.util.Date;
import java.util.List;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente propósito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 03/10/2013
 * 
 */
public class History
{
    private String title;

    private String picture;

    private String address;

    private Date startDate;

    private Date finishDate;

    private List<Jogador> players;

    public History(String title, String picture, String address, Date startDate, Date finishDate, List<Jogador> players)
    {
        super();
        this.title = title;
        this.picture = picture;
        this.address = address;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.players = players;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getPicture()
    {
        return picture;
    }

    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(Date finishDate)
    {
        this.finishDate = finishDate;
    }

    public List<Jogador> getPlayers()
    {
        return players;
    }

    public void setPlayers(List<Jogador> players)
    {
        this.players = players;
    }

}
