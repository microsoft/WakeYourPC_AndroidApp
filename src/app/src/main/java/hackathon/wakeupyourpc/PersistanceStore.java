package hackathon.wakeupyourpc;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.lang.reflect.Type;

/**
 * Created by girish on 7/26/2016.
 */

public class PersistanceStore {

    public static String UserFile = "User.dat";
    public static String WakeupRequestsFile = "WakeupRequests.dat";
    //public static String MachinesFile = "User.dat";

    public static <T> void WriteToFile(Context context, String fileName, T dataToWrite)
    {
        Gson jsonSerializer = new Gson();
        String jsonData = jsonSerializer.toJson(dataToWrite);

        try {
            FileOutputStream outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            FileWriter fileWriter = new FileWriter(outputStream.getFD());
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(jsonData);
            writer.newLine();
            writer.flush();
            writer.close();

            Toast.makeText(context, "Data Saved successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> T ReadFromFile(Context context, String fileName, Class<T> type)
    {
        String dataStored = ReadFromFileInternal(context, fileName);
        Gson jsonSerializer = new Gson();
        return jsonSerializer.fromJson(dataStored, type);
    }

    public static <T> T ReadFromFile(Context context, String fileName, Type type)
    {
        String dataStored = ReadFromFileInternal(context, fileName);
        Gson jsonSerializer = new Gson();
        return jsonSerializer.fromJson(dataStored, type);
    }

    private static String ReadFromFileInternal(Context context, String fileName)
    {
        String dataStored = "";
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            FileReader fileReader = new FileReader(inputStream.getFD());
            BufferedReader reader = new BufferedReader(fileReader);
            dataStored = reader.readLine();
            reader.close();

            Toast.makeText(context, "Data Read successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataStored;
    }
}
