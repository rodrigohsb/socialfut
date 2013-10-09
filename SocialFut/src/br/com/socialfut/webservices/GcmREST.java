package br.com.socialfut.webservices;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.socialfut.util.Constants;

public class GcmREST
{

    public static void sendMessage(String msg, long friendId)
    {
        JSONObject jsonObject = new JSONObject();
        String[] resposta = null;
        try
        {
            jsonObject.put("from", Constants.USER_ID);
            jsonObject.put("to", friendId);
            String json = jsonObject.toString();
            resposta = WebServiceClient.post(Constants.URL_GCM_WS + "chat", json);
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
