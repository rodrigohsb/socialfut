package br.com.socialfut.persistence;

import java.util.Comparator;

public class PosicaoComparator implements Comparator<Jogador>
{

    @Override
    public int compare(Jogador jogador1, Jogador jogador2)
    {
        String posicao1 = jogador1.getPosition();

        String posicao2 = jogador2.getPosition();

        int result = posicao1.compareTo(posicao2);

        return result;
    }

}