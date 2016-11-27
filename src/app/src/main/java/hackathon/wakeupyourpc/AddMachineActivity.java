package hackathon.wakeupyourpc;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

public class AddMachineActivity extends AppCompatActivity {

    private EditText machineIDTextView;
    private EditText machineNameTextView;
    private EditText machineMacAddressTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_machine);

        machineIDTextView = (EditText) findViewById(R.id.createMachineId);
        machineNameTextView = (EditText) findViewById(R.id.createMachineName);
        machineMacAddressTextView = (EditText) findViewById(R.id.createMacAddress);

        Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //Intent i = new Intent(AddMachineActivity.this, MainActivity.class);
                //startActivity(i);
                //finish();
            }
        });

        Button createButton = (Button) findViewById(R.id.addMachineButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateParameters();
            }
        });
    }

    private void ValidateParameters() {
        machineIDTextView.setError(null);
        machineNameTextView.setError(null);
        machineMacAddressTextView.setError(null);

        // Store values at the time of the login attempt.
        String machineIdInputText = machineIDTextView.getText().toString();
        String machineNameInputText = machineNameTextView.getText().toString();
        String machineMacInputText = machineMacAddressTextView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        UserData currentUserData = UserData.GetInstance();

        // Check for a valid machineID and machine Name
        if (TextUtils.isEmpty(machineIdInputText)) {
            machineIDTextView.setError(getString(R.string.error_field_required));
            focusView = machineIDTextView;
            cancel = true;
        } else {
            if (currentUserData != null && currentUserData.GetMachineById(machineIdInputText) != null) {
                machineIDTextView.setError(getString(R.string.error_machineId_present));
                focusView = machineIDTextView;
                cancel = true;
            }
        }

        if (TextUtils.isEmpty(machineNameInputText)) {
            machineNameTextView.setError(getString(R.string.error_field_required));
            focusView = machineNameTextView;
            cancel = true;
        }

        if (TextUtils.isEmpty(machineMacInputText)) {
            machineMacAddressTextView.setError(getString(R.string.error_field_required));
            focusView = machineMacAddressTextView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            Contract.Machine newMachine = new Contract.Machine();
            newMachine.MachineName = machineIdInputText;
            newMachine.HostName = machineNameInputText;
            newMachine.MacAddress = machineMacInputText;
            newMachine.ShouldWakeup = false;
            newMachine.State = Contract.MachineState.Unknown.toString();

            if(currentUserData != null)
            {
                currentUserData.AddMachine(newMachine);
            }

            AddMachineTask task = new AddMachineTask(newMachine, getApplicationContext());
            task.execute((Void) null);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class AddMachineTask extends AsyncTask<Void, Void, Boolean> {

        private Contract.Machine machineInfo;
        private Context appContext;

        AddMachineTask(Contract.Machine machineInfo, Context appContext) {
            this.machineInfo = machineInfo;
            this.appContext = appContext;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                ExternalConnector.AddOrUpdateMachine(machineInfo);
            }catch (IOException ex)
            {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                //Toast.makeText(appContext, "Registration Successful", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddMachineActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                machineIDTextView.setError(getString(R.string.error_add_machine_failed));
                machineIDTextView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
