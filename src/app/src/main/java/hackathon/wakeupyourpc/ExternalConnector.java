package hackathon.wakeupyourpc;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by girish on 7/27/2016.
 */
public class ExternalConnector {
    private static final String BASE_URL = "http://wakeyourpc.cloudapp.net/v1/";
    private static final String USERS_SUFFIX = "Users";
    private static final String MACHINES_SUFFIX = "Users/%s/machines";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-16");

    //private static OkHttpClient httpClient = new OkHttpClient();

    public static void AddUser(Contract.User userToAdd) throws IOException
    {
        String uriSuffix = USERS_SUFFIX + "/" + userToAdd.UserName;
        PutResource(uriSuffix, userToAdd, Contract.User.class);
        return;
    }

    public static Contract.User GetUser(String userName) throws IOException
    {
        String uriSuffix = USERS_SUFFIX + "/" + userName;
        return GetResource(uriSuffix,Contract.User.class);
    }

    public static void AddOrUpdateMachine(Contract.Machine machineInfo) throws IOException
    {
        UserData currentUserData = UserData.GetInstance();
        if(currentUserData == null || currentUserData.GetAppUser() == null)
        {
            return;
        }

        String urlSuffix = String.format(MACHINES_SUFFIX, currentUserData.GetAppUser().UserName);
        urlSuffix = urlSuffix  + "/" + machineInfo.MachineName;

        PutResource(urlSuffix, machineInfo, Contract.Machine.class);
        return;
    }

    public static ArrayList<Contract.Machine> GetAllMachines() throws IOException
    {
        UserData currentUserData = UserData.GetInstance();
        if(currentUserData == null || currentUserData.GetAppUser() == null)
        {
            return null;
        }

        String urlSuffix = String.format(MACHINES_SUFFIX, currentUserData.GetAppUser().UserName);
        Type outputType = new TypeToken<ArrayList<Contract.Machine>>(){}.getType();
        return GetAllResources(urlSuffix, outputType);
    }

    public static Contract.Machine GetMachine(String machineId) throws IOException
    {
        UserData currentUserData = UserData.GetInstance();
        if(currentUserData == null || currentUserData.GetAppUser() == null)
        {
            return null;
        }

        String urlSuffix = String.format(MACHINES_SUFFIX, currentUserData.GetAppUser().UserName);
        urlSuffix = urlSuffix + "/" + machineId;

        return GetResource(urlSuffix, Contract.Machine.class);
    }

    private static <T> T GetResource(String uriSuffix, Class<T> type) throws IOException
    {
        String url = BASE_URL + uriSuffix;
        Request request = new Request.Builder().url(url).build();
        OkHttpClient httpClient = new OkHttpClient();
        Response response = httpClient.newCall(request).execute();

        Gson jsonSerializer = new Gson();
        return jsonSerializer.fromJson(response.body().string(), type);
    }

    private static <T> T GetAllResources(String uriSuffix, Type type) throws IOException
    {
        String url = BASE_URL + uriSuffix;
        Request request = new Request.Builder().url(url).build();
        OkHttpClient httpClient = new OkHttpClient();
        Response response = httpClient.newCall(request).execute();

        Gson jsonSerializer = new Gson();
        return jsonSerializer.fromJson(response.body().string(), type);
    }

    private static <T> void PutResource(String uriSuffix, T dataToPut, Class<T> type) throws  IOException
    {
        Gson jsonSerializer = new Gson();
        String json = jsonSerializer.toJson(dataToPut, type);

        String url = BASE_URL + uriSuffix;
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).put(body).build();
        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(request).execute();

        return;
    }
}
