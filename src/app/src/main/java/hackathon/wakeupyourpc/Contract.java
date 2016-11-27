package hackathon.wakeupyourpc;

import java.util.Date;

/**
 * Created by girish on 7/26/2016.
 */
public final class Contract {

    public enum MachineState
    {
        Available,
        Asleep,
        WakingUp,
        Unknown
    }

    public static class User
    {
        public String UserName;
        public String Password;
    }

    public static class Machine
    {
        public String MachineName;
        public String HostName;
        public String MacAddress;
        public boolean ShouldWakeup;
        public String State;
        public String UserName;
    }

    public static class WakeUpCall
    {
        public String RequestID;
        public String MachineID;
        public String RequestTime;
    }
}
