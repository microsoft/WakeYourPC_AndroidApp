package hackathon.wakeupyourpc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;

public class MachineInfoActivity extends AppCompatActivity {

    private Contract.Machine machineInfo = null;
    private EditText machineIdTextView;
    private EditText machineNameTextView;
    private EditText machineMacTextView;
    private EditText machineStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_info);

        Bundle args = getIntent().getExtras();

        if (args != null) {
            String machineId = args.getString("MachineId");
            UserData currentUser = UserData.GetInstance();
            if (currentUser != null) {
                machineInfo = currentUser.GetMachineById(machineId);
            }
        }

        machineIdTextView = (EditText) findViewById(R.id.viewMachineID);
        machineIdTextView.setText(machineInfo.MachineName);

        machineNameTextView = (EditText) findViewById(R.id.viewMachineName);
        machineNameTextView.setText(machineInfo.HostName);

        machineMacTextView = (EditText) findViewById(R.id.viewMacAddress);
        machineMacTextView.setText(machineInfo.MacAddress);

        machineStateTextView = (EditText) findViewById(R.id.viewMachineState);
        machineStateTextView.setText(machineInfo.State);

        Button wakeupButton = (Button) findViewById(R.id.wakeUpButton);
        wakeupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract.WakeUpCall wakeupCall = new Contract.WakeUpCall();
                Date now = new Date();
                wakeupCall.RequestID = machineInfo.MachineName + Long.toString(now.getTime());
                wakeupCall.MachineID = machineInfo.MachineName;
                wakeupCall.RequestTime = now.toString();

                UserData currentUserData = UserData.GetInstance();
                if (currentUserData != null) {
                    currentUserData.AddWakeupCall(wakeupCall);
                }

                machineInfo.ShouldWakeup = true;
                PersistanceStore.WriteToFile(MachineInfoActivity.this, PersistanceStore.WakeupRequestsFile, currentUserData.GetAllWakeupCalls());
                WakeupMachineTask task = new WakeupMachineTask(machineInfo, MachineInfoActivity.this);
                task.execute((Void) null);
            }
        });

        Button refreshButton = (Button) findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMachineTask machineTask = new GetMachineTask(machineInfo, MachineInfoActivity.this);
                machineTask.execute((Void) null);
            }
        });
    }

    private void UpdateMachineInfo(Contract.Machine newMachineInfo)
    {
        this.machineInfo = newMachineInfo;
        UserData currentUser = UserData.GetInstance();

        if(currentUser != null)
        {
            currentUser.AddMachine(newMachineInfo);
        }
    }

    private void RefreshTextViews() {
        machineIdTextView.setText(machineInfo.MachineName);
        machineNameTextView.setText(machineInfo.HostName);
        machineMacTextView.setText(machineInfo.MacAddress);
        machineStateTextView.setText(machineInfo.State);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class WakeupMachineTask extends AsyncTask<Void, Void, Boolean> {

        private Contract.Machine machineInfo;
        private Context appContext;

        WakeupMachineTask(Contract.Machine machineInfo, Context appContext) {
            this.machineInfo = machineInfo;
            this.appContext = appContext;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                ExternalConnector.AddOrUpdateMachine(machineInfo);
            } catch (IOException ex) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast.makeText(appContext, "Wake up call Initiated.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MachineInfoActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(appContext, "Wake up call failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class GetMachineTask extends AsyncTask<Void, Void, Boolean> {

        private Contract.Machine machineInfo;
        private Context appContext;

        GetMachineTask(Contract.Machine machineInfo, Context appContext) {
            this.machineInfo = machineInfo;
            this.appContext = appContext;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                machineInfo = ExternalConnector.GetMachine(machineInfo.MachineName);
            } catch (IOException ex) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
                if (success) {
                    UpdateMachineInfo(machineInfo);
                    RefreshTextViews();
                }
//                    //Toast.makeText(appContext, "Registration Successful", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(MachineInfoActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                } else {
//                    Toast.makeText(appContext, "Wake up call failed", Toast.LENGTH_SHORT).show();
//                }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
