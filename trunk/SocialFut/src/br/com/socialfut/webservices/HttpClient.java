package br.com.socialfut.webservices;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * 
 * <b>Descricao da Classe:</b><br>
 * TODO Explicar detalhadamente proposito da classe.
 * 
 * @author rodrigo.bacellar
 * @since 29/09/2013
 * 
 */
public class HttpClient
{

    private static final int JSON_CONNECTION_TIMEOUT = 5000;

    private static final int JSON_SOCKET_TIMEOUT = 5000;

    private static HttpClient instance;

    private HttpParams httpParameters;

    private DefaultHttpClient httpclient;

    private void setTimeOut(HttpParams params)
    {
        HttpConnectionParams.setConnectionTimeout(params, JSON_CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, JSON_SOCKET_TIMEOUT);
    }

    private HttpClient()
    {
        httpParameters = new BasicHttpParams();
        setTimeOut(httpParameters);
        httpclient = new DefaultHttpClient(httpParameters);
    }

    public static DefaultHttpClient getHttpClientInstace()
    {
        if (instance == null) instance = new HttpClient();
        return instance.httpclient;
    }

}
