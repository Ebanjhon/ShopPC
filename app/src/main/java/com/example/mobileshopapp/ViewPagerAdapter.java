package com.example.mobileshopapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final String[] tabTitles = new String[]{"thông tin User", "Lịch sử đơn"};

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProfileDetailFragment();
            case 1:
                return new ListOrderFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }

    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
