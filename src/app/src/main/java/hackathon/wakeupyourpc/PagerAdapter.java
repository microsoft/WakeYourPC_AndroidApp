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
        MachinesTabFragment tab;
        switch (position) {
            case 0:
                tab = MachinesTabFragment.newInstance(TabType.MachinesTab);
            case 1:
                tab = MachinesTabFragment.newInstance(TabType.RequestsTab);
            default:
                return null;
        }
        return tab;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
