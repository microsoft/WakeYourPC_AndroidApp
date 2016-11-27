package hackathon.wakeupyourpc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

enum TabType
{
    MachinesTab,
    RequestsTab,
}

public class MachinesTabFragment extends Fragment {
    private TabType thisTabType;
    private ListView currentTabListView;

    public MachinesTabFragment() {
    }

    public void setTabType(TabType tabType)
    {
        thisTabType = tabType;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MachinesTabFragment.
     */
    public static MachinesTabFragment newInstance(TabType tabType) {
        MachinesTabFragment fragment = new MachinesTabFragment();
        fragment.setTabType(tabType);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_machines_tab, container, false);
        currentTabListView = (ListView) v.findViewById(R.id.genericListView);
        populateListView();
        return  v;
    }

    private void populateListView()
    {
        UserData currentUserData = UserData.GetInstance();
        switch (thisTabType)
        {
            case MachinesTab:
                ArrayList<String> machinesListToShow = new ArrayList<>();
                if(currentUserData != null)
                {
                    for(Contract.Machine currentMachine : currentUserData.GetAllMachines())
                    {
                        machinesListToShow.add(currentMachine.MachineName);
                    }

                }
                ArrayAdapter<String> machinesListViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, machinesListToShow);
                currentTabListView.setAdapter(machinesListViewAdapter);

                currentTabListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i = new Intent(getActivity(), MachineInfoActivity.class);
                        TextView currentRow = (TextView) view.findViewById(android.R.id.text1);
                        i.putExtra("MachineId", currentRow.getText().toString().trim());
                        startActivity(i);
                        //getActivity().finish();
                        return;
                    }
                });
                break;
            case RequestsTab:
                ArrayList<String> wakeUpRequests = new ArrayList<>();
                if(currentUserData != null)
                {
                    for(Contract.WakeUpCall wakupCall : currentUserData.GetAllWakeupCalls())
                    {
                        wakeUpRequests.add("Wakeup " + wakupCall.MachineID);
                    }
                }
                ArrayAdapter<String> requestListViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, wakeUpRequests);
                currentTabListView.setAdapter(requestListViewAdapter);
                break;
        }
    }
}
