package hackathon.wakeupyourpc;

/**
 * Created by girish on 7/26/2016.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Machines";
            case 1:
                return "Requests";
        }
        return null;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MachinesTabFragment tab1 = MachinesTabFragment.newInstance(TabType.MachinesTab);
                return tab1;
            case 1:
                MachinesTabFragment tab2 = MachinesTabFragment.newInstance(TabType.RequestsTab);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
