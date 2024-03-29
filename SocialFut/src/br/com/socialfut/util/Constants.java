package br.com.socialfut.util;

import java.util.List;

import br.com.socialfut.persistence.Player;

public class Constants
{

    /** CONSTANTES BASICAS [INICIO] */

    public static final String OK = "OK";

    public static final String YES = "Sim";

    public static final String NO = "Nao";

    public static final String CLOSE_APP = "Deseja sair?";

    public static final String WARNING = "Aviso";

    public static final String NO_FRIEND = "Nenhum amigo encontrado.";

    public static final String NO_OLD_GAMES = "Nenhuma partida foi disputada.";

    public static final String NO_NEW_GAMES = "Nenhuma partida a ser disputada.";

    public static final String WEBSERVICES_DOWN = "N�o foi poss�vel recuperar o hist�rico.";

    public static final String END_BEFORE_START = "O hor�rio de t�rmino precisa ser posterior ao de in�cio.";

    public static final String PROJECT_NUMBER = "281054247643";

    public static List<Player> jogadores;

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final String HOUR_PATTERN = "HH:mm";

    public static final String DATE_PATTERN_FOR_USER = "dd / MM / yyyy";

    public static final String HOUR_PATTERN_FOR_USER = "HH:mm";

    public static long TIME;

    /** CONSTANTES BASICAS [FIM] */

    /** WEB SERVICES [INICIO] */

    public static final String BASE_URL = "http://social-fut.no-ip.biz:8080/SocialFutServer/";

    public static final String URL_GAME_WS = BASE_URL + "game/";

    public static final String URL_PLAYER_WS = BASE_URL + "player/";

    public static final String URL_CHAT_WS = BASE_URL + "chat/";

    public static final String WS_STATUS_OK = "OK";

    public static long USER_ID;

    public static String DEVICE_REGISTRATION_ID;

    public static int POSITION_ID;

    public static final String CONFIRMATION = "confirmation";

    public static final String DESCONFIRMATION = "desconfirmation";

    public static final String INVITATION = "invitation";

    public static final String GCM_INTENT_SERVICE = "br.com.socialfut.gcm.GCMIntentService";

    public static final String SEMICOLON = ";";

    /** WEB SERVICES [FIM] */

    /** FQL [INICIO] */
    public static final String FRIENDS_USES_APP = "select uid,first_name,last_name,pic_square,is_app_user from user where uid in (select uid2 from friend where uid1 = me())";

    public static final String FRIENDS_BY_ID = "select uid,first_name,last_name,pic_square from user where uid in ({0})";

    public static final String Q = "q";

    public static final String SLASH = "/";

    public static final String FQL = "fql";

    public static final String UID = "uid";

    public static final String FIRST_NAME = "first_name";

    public static final String LAST_NAME = "last_name";

    public static final String PIC_SQUARE = "pic_square";

    public static final String IS_APP_USER = "is_app_user";

    /** FQL [FIM] */

}
