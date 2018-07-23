package com.example.admin.svsplacement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHelper extends FragmentPagerAdapter{
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    int noOfTabs;
    public FragmentHelper(FragmentManager supportFragmentManager,int noOfTabs) {
        super(supportFragmentManager);
        this.noOfTabs = noOfTabs;
    }
    public FragmentHelper(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
//        switch(position){
//            case 0:
//                Tab_1 tab_1 = new Tab_1();
//                return tab_1;
//            case 1:
//                Tab_2 tab_2 = new Tab_2();
//                return tab_2;
//            case 2:
//                Tab_3 tab_3 = new Tab_3();
//                return tab_3;
//            case 3:
//                Tab_4 tab_4 = new Tab_4();
//                return tab_4;
//            default:
//                return null;
//        }
        return mFragmentList.get(position);
    }
    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}