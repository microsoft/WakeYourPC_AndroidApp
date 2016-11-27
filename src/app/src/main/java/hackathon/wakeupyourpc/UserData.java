package hackathon.wakeupyourpc;

import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by girish on 7/26/2016.
 */
public class UserData {

    private Contract.User currentUser = null;
    private Map<String, Contract.Machine> machinesList = null;
    private Map<String, Contract.WakeUpCall> wakeUpHistory = null;

    private static UserData thisInstance = null;

    private UserData(Contract.User user)
    {
        this.currentUser = user;
        this.machinesList = new HashMap<>();
        this.wakeUpHistory = new HashMap<>();
    }

    public static UserData CreateInstance(Contract.User user)
    {
        if(thisInstance == null && user != null)
        {
            thisInstance = new UserData(user);
        }

        return thisInstance;
    }

    public static UserData GetInstance()
    {
        return thisInstance;
    }

    public void AddMachine(Contract.Machine machineToAdd)
    {
        if(machineToAdd == null || machineToAdd.MachineName == null || machineToAdd.MachineName.isEmpty())
            return;

        machinesList.put(machineToAdd.MachineName, machineToAdd);
    }

    public void AddMachines(ArrayList<Contract.Machine> machinesToAdd)
    {
        if(machinesToAdd == null || machinesToAdd.size() == 0)
            return;

        for(Contract.Machine currentMachine : machinesToAdd)
        {
            if(currentMachine.MachineName == null || currentMachine.MachineName.isEmpty())
                continue;

            this.machinesList.put(currentMachine.MachineName, currentMachine);
        }
    }

    public void AddWakeupCall(Contract.WakeUpCall wakeupCall)
    {
        if(wakeupCall == null || wakeupCall.RequestID == null || wakeupCall.RequestID.isEmpty())
            return;

        this.wakeUpHistory.put(wakeupCall.RequestID, wakeupCall);
    }

    public void AddWakeupCalls(ArrayList<Contract.WakeUpCall> wakeupCalls)
    {
        if(wakeupCalls == null || wakeupCalls.size() == 0)
            return;

        for(Contract.WakeUpCall wakeupCall : wakeupCalls)
        {
            if(wakeupCall.RequestID == null || wakeupCall.RequestID.isEmpty())
                continue;

            this.wakeUpHistory.put(wakeupCall.RequestID, wakeupCall);
        }
    }

    public void RemoveWakeupCalls(ArrayList<String> wakeupCallIdList)
    {
        if(wakeupCallIdList == null || wakeupCallIdList.size() == 0)
         return;

        for(String currentWakeUpCallId : wakeupCallIdList)
        {
            if(this.wakeUpHistory.containsKey(currentWakeUpCallId))
            {
                this.wakeUpHistory.remove(currentWakeUpCallId);
            }
        }
    }

    public void RemoveMachines(ArrayList<String> machineIds)
    {
        if(machineIds == null || machineIds.size() == 0)
            return;

        for(String currentMachineId : machineIds)
        {
            if(this.machinesList.containsKey(currentMachineId))
            {
                this.machinesList.remove(currentMachineId);
            }
        }
    }

    public Contract.User GetAppUser()
    {
        return this.currentUser;
    }

    public ArrayList<Contract.Machine> GetAllMachines()
    {
        ArrayList<Contract.Machine> returnList = new ArrayList<>();
        returnList.addAll(this.machinesList.values());
        return returnList;
    }

    public Contract.Machine GetMachineById(String machineID)
    {
        String key = machineID.trim();
        if(this.machinesList.containsKey(key))
        {
            return this.machinesList.get(key);
        }

        return null;
    }

    public ArrayList<Contract.WakeUpCall> GetAllWakeupCalls()
    {
        ArrayList<Contract.WakeUpCall> returnList = new ArrayList<>();
        returnList.addAll(this.wakeUpHistory.values());
        return returnList;
    }
}
