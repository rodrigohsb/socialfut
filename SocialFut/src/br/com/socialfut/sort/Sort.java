package br.com.socialfut.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.socialfut.persistence.Player;
import br.com.socialfut.persistence.PlayerComparator;
import br.com.socialfut.persistence.PosicaoComparator;
import br.com.socialfut.util.ConstantsEnum;

public class Sort
{
    private static float totalTimeA = 0;

    private static float totalTimeB = 0;

    public static Map<ConstantsEnum, List<Player>> getOrder(List<Player> jogadores)
    {
        Map<ConstantsEnum, List<Player>> map = new HashMap<ConstantsEnum, List<Player>>();

        List<Player> timeA = new ArrayList<Player>();

        List<Player> timeB = new ArrayList<Player>();

        /** Ordenar a lista por posicao. */
        applySortPosicao(jogadores);

        /** Obtem a lista ordenada por posicao e agora ordena pela classificacao.*/
        applySort(jogadores);

        for (Player jogador : jogadores)
        {
            if (timeA.size() == timeB.size())
            {
                if (totalTimeA > totalTimeB)
                {
                    timeB.add(jogador);
                    totalTimeB = totalTimeB + jogador.getRating();
                }
                else if (totalTimeA < totalTimeB)
                {
                    timeA.add(jogador);
                    totalTimeA = totalTimeA + jogador.getRating();
                }
                else
                {
                    timeB.add(jogador);
                    totalTimeB = totalTimeB + jogador.getRating();
                }
            }
            else if (timeA.size() > timeB.size())
            {
                timeB.add(jogador);
                totalTimeB = totalTimeB + jogador.getRating();
            }
            else if (timeA.size() < timeB.size())
            {
                timeA.add(jogador);
                totalTimeA = totalTimeA + jogador.getRating();
            }
        }

        map.put(ConstantsEnum.TIMEA, timeA);

        map.put(ConstantsEnum.TIMEB, timeB);

        return map;
    }

    public static void applySort(List<Player> jogadores)
    {
        PlayerComparator jogadorComparator = new PlayerComparator(PlayerComparator.DESCEDANT);

        Collections.sort(jogadores, jogadorComparator);
    }

    public static void applySortPosicao(List<Player> jogadores)
    {
        PosicaoComparator posicaoComparator = new PosicaoComparator();

        Collections.sort(jogadores, posicaoComparator);
    }
}
