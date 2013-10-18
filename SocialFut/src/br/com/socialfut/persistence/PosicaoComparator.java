package br.com.socialfut.persistence;

import java.util.Comparator;

public class PosicaoComparator implements Comparator<Player>
{

    @Override
    public int compare(Player jogador1, Player jogador2)
    {
        String posicao1 = jogador1.getPosition();

        String posicao2 = jogador2.getPosition();

        int result = posicao1.compareTo(posicao2);

        return result;
    }

}