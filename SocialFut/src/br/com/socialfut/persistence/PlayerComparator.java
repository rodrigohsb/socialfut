package br.com.socialfut.persistence;

import java.util.Comparator;

public class PlayerComparator implements Comparator<Player>
{
    public static final String ASCENDANT = "ASC";

    public static final String DESCEDANT = "DESC";

    private String order;

    public PlayerComparator(String order)
    {
        this.order = order;
    }

    public PlayerComparator()
    {

    }

    @Override
    public int compare(Player jogador1, Player jogador2)
    {
        String posicao1 = jogador1.getPosition();

        String posicao2 = jogador2.getPosition();

        Float classificacao1 = jogador1.getRating();

        Float classificacao2 = jogador2.getRating();

        if (getOrder() == null || ASCENDANT.equals(getOrder()))
        {
            int result = posicao1.compareTo(posicao2);

            return result == 0 ? classificacao1.compareTo(classificacao2) : result;
        }
        else if (DESCEDANT.equals(getOrder()))
        {
            int result = posicao2.compareTo(posicao1);

            return result == 0 ? classificacao2.compareTo(classificacao1) : result;
        }

        return 0;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }
}