package br.com.socialfut.webservices;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.com.socialfut.util.Constants;

public class PlayerREST extends AsyncTask<Void, Void, String[]>
{

    public static void sendRegistrationPOST()
    {
        JSONObject jsonObject = new JSONObject();
        String[] resposta = null;
        try
        {
            jsonObject.put("facebookId", Constants.USER_ID);
            jsonObject.put("deviceRegId", Constants.DEVICE_REGISTRATION_ID);
            String json = jsonObject.toString();
            resposta = WebServiceClient.post(Constants.URL_PLAYER_WS + "inserir", json);
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

    public static String[] sendRegistrationGET()
    {
        String[] resposta = WebServiceClient.get(Constants.URL_PLAYER_WS + "insert" + "/" + Constants.USER_ID + "/"
                + Constants.DEVICE_REGISTRATION_ID);
        if (resposta[0].equals(Constants.WS_STATUS))
        {
            System.out.println(resposta[0]);
        }
        else
        {
            System.err.println(resposta[1]);
        }
        return resposta;
    }

    @Override
    protected String[] doInBackground(Void... params)
    {
        return sendRegistrationGET();
    }

    // public Jogador getPlayerDetails(Jogador j) throws Exception
    // {
    // String[] resposta = WebServiceClient.get(Constants.URL_WS + j.getId());
    //
    // if (resposta[0].equals(Constants.WS_STATUS))
    // {
    // Gson gson = new Gson();
    // Cliente cliente = gson.fromJson(resposta[1], Cliente.class);
    // return j;
    // }
    // else
    // {
    // throw new Exception(resposta[1]);
    // }
    // }
    //
    // public String insertPlayer(String deviceRegId) throws Exception
    // {
    //
    // Gson gson = new Gson();
    // String clienteJSON = gson.toJson(cliente);
    // JSONObject jsonObject = new JSONObject();
    // jsonObject.put("facebookId", Constants.USER_ID);
    // jsonObject.put("deviceRegId", deviceRegId);
    // String json = jsonObject.toString();
    // String[] resposta = WebServiceClient.post(Constants.URL_WS + "inserir",
    // json);
    //
    // if (resposta[0].equals(Constants.WS_STATUS))
    // {
    // return resposta[1];
    // }
    // else
    // {
    // throw new Exception(resposta[1]);
    // }
    // }
    //
    // public List<Cliente> getListaCliente() throws Exception
    // {
    //
    // String[] resposta = new WebServiceClient().get(Constants.URL_WS +
    // "buscarTodosGSON");
    // // String[] resposta = new WebServiceCliente().get(URL_WS +
    // // "buscarTodos");
    //
    // if (resposta[0].equals(Constants.WS_STATUS))
    // {
    // Gson gson = new Gson();
    // ArrayList<Cliente> listaCliente = new ArrayList<Cliente>();
    // JsonParser parser = new JsonParser();
    // JsonArray array = parser.parse(resposta[1]).getAsJsonArray();
    //
    // for (int i = 0; i < array.size(); i++)
    // {
    // listaCliente.add(gson.fromJson(array.get(i), Cliente.class));
    // }
    // return listaCliente;
    // }
    // else
    // {
    // throw new Exception(resposta[1]);
    // }
    // }
    //
    // public String deletarCliente(int id)
    // {
    // String[] resposta = new WebServiceClient().get(Constants.URL_WS +
    // "delete/" + id);
    // return resposta[1];
    // }
}