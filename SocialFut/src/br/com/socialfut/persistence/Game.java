package br.com.socialfut.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente proposito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 03/10/2013
 * 
 */
public class Game implements Serializable
{
    private static final long serialVersionUID = 1L;

    private long id;

    private String title;

    private String address;

    private Date createdDate;

    private Date startDate;

    private Date finishDate;

    public Game()
    {
        super();
    }

    public Game(String title, String address, Date createdDate, Date startDate, Date finishDate)
    {
        super();
        this.title = title;
        this.address = address;
        this.createdDate = createdDate;
        this.startDate = startDate;
        this.finishDate = finishDate;

    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
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

    @Override
    public String toString()
    {
        return super.toString();
    }
}
