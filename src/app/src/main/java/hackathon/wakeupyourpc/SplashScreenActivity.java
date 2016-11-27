package hackathon.wakeupyourpc;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SplashScreenActivity extends AppCompatActivity implements  Runnable{

    private  static int SPLASH_TIME_OUT = 5000;

    private Class nextActivityClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //new Handler().postDelayed(this, SPLASH_TIME_OUT);
        InitializeUserDataStore();

        if(UserData.GetInstance() == null || UserData.GetInstance().GetAppUser() == null)
        {
            nextActivityClass = RegisterActivity.class;
        }
        else
        {
            nextActivityClass = MainActivity.class;
        }

        StartupTask startupTask = new StartupTask(this);
        startupTask.execute((Void) null);
    }

    private void InitializeUserDataStore()
    {
        Contract.User currentAppUser = PersistanceStore.ReadFromFile(getApplicationContext(), PersistanceStore.UserFile, Contract.User.class);
        if(currentAppUser == null)
            return;

        UserData currentUserData = UserData.CreateInstance(currentAppUser);

        Type wakeupListClass = new TypeToken<ArrayList<Contract.WakeUpCall>>(){}.getType();
        ArrayList<Contract.WakeUpCall> wakeupHistory = (ArrayList<Contract.WakeUpCall>) PersistanceStore.ReadFromFile(getApplicationContext(), PersistanceStore.WakeupRequestsFile, wakeupListClass);
        //ArrayList<Contract.WakeUpCall> wakeupHistory = new ArrayList<>();
        currentUserData.AddWakeupCalls(wakeupHistory);


//        Type wakeupListClass = new TypeToken<ArrayList<Contract.WakeUpCall>>(){}.getType();
//        ArrayList<Contract.WakeUpCall> wakeupHistory = (ArrayList<Contract.WakeUpCall>) PersistanceStore.ReadFromFile(getApplicationContext(), PersistanceStore.WakeupRequestsFile, wakeupListClass);
//        currentUserData.AddWakeupCalls(wakeupHistory);
//
//        try{
//            ArrayList<Contract.Machine> machinesList = ExternalConnector.GetAllMachines();
//            currentUserData.AddMachines(machinesList);
//        }
//        catch (IOException ex)
//        {
//
//        }
    }

    @Override
    public void run() {
        Intent i = new Intent(SplashScreenActivity.this, nextActivityClass);
        startActivity(i);
        finish();
    }

    public class StartupTask extends AsyncTask<Void, Void, Boolean> {
        private Context appContext;

        StartupTask(Context appContext) {
            this.appContext = appContext;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
                    // Register the new account here.
        //        try{
        //            Contract.User user= ExternalConnector.GetUser(currentAppUser.UserName);
        //            if(user == null)
        //                return;
        //        }
        //        catch (IOException ex)
        //        {
        //
        //        }

            try{
                UserData currentUserData = UserData.GetInstance();
                if(currentUserData != null) {
                    ArrayList<Contract.Machine> machinesList = ExternalConnector.GetAllMachines();
                    currentUserData.AddMachines(machinesList);
                }
            }
            catch (IOException ex)
            {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(appContext, "App Initialization Successful", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(appContext, "App Initialization UnSuccessful", Toast.LENGTH_SHORT).show();
            }

        Intent i = new Intent(SplashScreenActivity.this, nextActivityClass);
            startActivity(i);
            finish();
        }

        @Override
        protected void onCancelled() {
        }
    }
}
