package sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import persistence.Jogador;
import persistence.JogadorComparator;
import persistence.PosicaoComparator;

import util.ConstantsEnum;

public class Sort
{
    private static float totalTimeA = 0;

    private static float totalTimeB = 0;

    public static Map<ConstantsEnum, List<Jogador>> getOrder(List<Jogador> jogadores)
    {
        Map<ConstantsEnum, List<Jogador>> map = new HashMap<ConstantsEnum, List<Jogador>>();

        List<Jogador> timeA = new ArrayList<Jogador>();

        List<Jogador> timeB = new ArrayList<Jogador>();

        /** Ordenar a lista por posicao. */
        applySortPosicao(jogadores);

        /** Obtem a lista ordenada por posicao e agora ordena pela classificacao.*/
        applySort(jogadores);

        for (Jogador jogador : jogadores)
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

    public static void applySort(List<Jogador> jogadores)
    {
        JogadorComparator jogadorComparator = new JogadorComparator(JogadorComparator.DESCEDANT);

        Collections.sort(jogadores, jogadorComparator);
    }

    public static void applySortPosicao(List<Jogador> jogadores)
    {
        PosicaoComparator posicaoComparator = new PosicaoComparator();

        Collections.sort(jogadores, posicaoComparator);
    }
}
