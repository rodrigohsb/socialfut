package br.com.socialfut.webservices;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.socialfut.util.Constants;

public class WebService
{

    public static void sendRegistration()
    {
        JSONObject jsonObject = new JSONObject();
        String[] resposta = null;
        try
        {
            jsonObject.put("facebookId", Constants.USER_ID);
            jsonObject.put("deviceRegId", Constants.DEVICE_REGISTRATION_ID);
            String json = jsonObject.toString();
            resposta = WebServiceClient.post(Constants.URL_WS + "inserir", json);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (resposta[0].equals(Constants.WS_STATUS))
        {
            System.out.println(resposta[0]);
        }
        else
        {
            System.err.println(resposta[1]);
        }
    }

    public static void sendMessage(long friendId)
    {
        JSONObject jsonObject = new JSONObject();
        String[] resposta = null;
        try
        {
            jsonObject.put("facebookId", Constants.USER_ID);
            jsonObject.put("deviceRegId", Constants.DEVICE_REGISTRATION_ID);
            jsonObject.put("friendId", friendId);
            String json = jsonObject.toString();
            resposta = WebServiceClient.post(Constants.URL_WS + "sendMessage", json);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if (resposta[0].equals(Constants.WS_STATUS))
        {
            System.out.println(resposta[0]);
        }
        else
        {
            System.err.println(resposta[1]);
        }
    }

}
