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

    public static final String PROJECT_NUMBER = "281054247643";

    public static final String GCM_INTENT_SERVICE = "br.com.socialfut.gcm.GCMIntentService";

    public static final String SEMICOLON = ";";

    public static long USER_ID = 583633830;

    public static List<Jogador> jogadores;

    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

//    public static final String URL_WS = "http://10.0.2.2:8080/WebServiceREST/cliente/";
    
    public static final String URL_WS = "http://192.168.0.11:8080/SocialFutServer/player/";

    public static final String WS_STATUS = "200";
}
