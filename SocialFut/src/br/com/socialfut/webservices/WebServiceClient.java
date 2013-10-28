package br.com.socialfut.webservices;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import android.util.Log;

public class WebServiceClient
{
    public static String[] get(String url)
    {

        String[] result = new String[2];
        HttpGet httpget = new HttpGet(url);
        HttpResponse response;

        try
        {
            System.out.println(httpget.getURI());
            response = HttpClient.getHttpClientInstance().execute(httpget);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                InputStream instream = entity.getContent();
                result[1] = toString(instream);
                instream.close();
                Log.i("get", "Result from GET : " + result[1]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result[0] = "0";
            result[1] = "Falha de rede!";
        }
        return result;
    }

    public static String[] put(String url, String msg)
    {

        String[] result = new String[2];
        HttpPut httpPut = new HttpPut(url);
        HttpResponse response;

        try
        {

            StringEntity mensagem = new StringEntity(msg);
            httpPut.setEntity(mensagem);

            System.out.println(httpPut.getURI());

            response = HttpClient.getHttpClientInstance().execute(httpPut);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                InputStream instream = entity.getContent();
                result[1] = toString(instream);
                instream.close();
                Log.i("get", "Result from PUT : " + result[1]);
            }
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result[0] = "0";
            result[1] = "Falha de rede!";
        }
        return result;
    }

    public static String[] put(String url, String title, String address)
    {

        String[] result = new String[2];
        HttpPut httpPut = new HttpPut(url);
        HttpResponse response;

        try
        {

            StringEntity param0 = new StringEntity(title);
            StringEntity param1 = new StringEntity(address);
            
            httpPut.setEntity(param0);
            httpPut.setEntity(param1);

            System.out.println(httpPut.getURI());

            response = HttpClient.getHttpClientInstance().execute(httpPut);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                InputStream instream = entity.getContent();
                result[1] = toString(instream);
                instream.close();
                Log.i("get", "Result from PUT : " + result[1]);
            }
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result[0] = "0";
            result[1] = "Falha de rede!";
        }
        return result;
    }

    public static String[] post(String url, String json)
    {
        String[] result = new String[2];
        try
        {
            HttpPost httpPost = new HttpPost(new URI(url));
            httpPost.setHeader("Content-type", "application/json");
            StringEntity sEntity = new StringEntity(json, "UTF-8");
            httpPost.setEntity(sEntity);

            System.out.println(httpPost.getURI());

            HttpResponse response = HttpClient.getHttpClientInstance().execute(httpPost);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                result[0] = String.valueOf(response.getStatusLine().getStatusCode());
                InputStream instream = entity.getContent();
                result[1] = toString(instream);
                instream.close();
                Log.d("post", "Result from POST: " + result[0] + " : " + result[1]);
            }

        }
        catch (Exception e)
        {
            result[0] = "0";
            result[1] = "Falha de rede!";
        }
        return result;
    }

    private static String toString(InputStream is) throws IOException
    {

        byte[] bytes = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int lidos;
        while ((lidos = is.read(bytes)) > 0)
        {
            baos.write(bytes, 0, lidos);
        }
        return new String(baos.toByteArray());
    }
}