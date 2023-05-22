package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import narasimhaa.com.mitraservice.DevelopersFragment;
import narasimhaa.com.mitraservice.MaterialFragment;

public class DashboardTabsAdapater extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public DashboardTabsAdapater(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                DevelopersFragment developersFragment = new DevelopersFragment();
                return developersFragment;
            case 0:
                MaterialFragment materialFragment = new MaterialFragment();
                return materialFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
