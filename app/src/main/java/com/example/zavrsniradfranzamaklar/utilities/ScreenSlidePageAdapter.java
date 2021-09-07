package com.example.zavrsniradfranzamaklar.utilities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.zavrsniradfranzamaklar.inputs.InputGeneral;
import com.example.zavrsniradfranzamaklar.inputs.InputSpecial;

import java.util.Locale;

public class ScreenSlidePageAdapter extends FragmentStatePagerAdapter {


    private static final int NUM_PAGES = 2;
    private static final String general = "Opći podatci";
    private static final String special = "Specifikacije smrti";


    public ScreenSlidePageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new InputGeneral();
        }else
            return new InputSpecial();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return String.format(Locale.getDefault(), general, position+1);
        }else {
            return String.format(Locale.getDefault(), special, position+1);
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}
