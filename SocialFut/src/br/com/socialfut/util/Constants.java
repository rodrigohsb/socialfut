package br.com.socialfut.util;

import java.util.List;

import br.com.socialfut.persistence.Jogador;

public class Constants
{

    public static final String NOME = "nome";

    public static final String ERRO = "erro";

    public static final String OK = "OK";

    public static final String CANCEL = "Cancelar";

    public static final String YES = "Sim";

    public static final String NO = "Nao";

    public static final String SUCESS = "Sucesso";

    public static final String CLOSE_APP = "Deseja sair?";

    public static final String WARNING = "Aviso";

    public static final String NO_FRIEND = "Nenhum amigo encontrado.";

    public static final String NO_OLD_GAMES = "Nenhuma partida foi disputada.";

    public static final String NO_NEW_GAMES = "Nenhuma partida a ser disputada.";

    public static final String PROJECT_NUMBER = "281054247643";

    public static final String GCM_INTENT_SERVICE = "br.com.socialfut.gcm.GCMIntentService";

    public static final String SEMICOLON = ";";

    public static final String SLASH = "/";

    public static long USER_ID = 583633830;

    public static List<Jogador> jogadores;

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String HOUR_PATTERN = "HH:mm";
    
    public static final String DATE_PATTERN_FOR_USER = "dd / MM / yyyy";

    public static final String HOUR_PATTERN_FOR_USER = "HH:mm";

    /** WEB SERVICES [INICIO] */

    public static final String URL_WS = "http://192.168.0.11:8080/SocialFutServer/player/";

    public static final String WS_STATUS = "200";

    /** WEB SERVICES [FIM] */

    /** FQL [INICIO] */
    public static final String FRIENDS_USES_APP = "select uid,first_name,last_name,pic_square,is_app_user from user where uid in (select uid2 from friend where uid1 = me())";

    public static final String FQL = "fql";

    public static final String UID = "uid";

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final String PIC_SQUARE = "pic_square";

    public static final String IS_APP_USER = "is_app_user";
    /** FQL [FIM] */
}
