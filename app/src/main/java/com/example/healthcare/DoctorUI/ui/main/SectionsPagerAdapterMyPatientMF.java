package com.example.healthcare.DoctorUI.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.healthcare.DoctorUI.MyPatientConsultationsFragment;
import com.example.healthcare.DoctorUI.MyPatientHospitalisationsFragment;
import com.example.healthcare.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterMyPatientMF extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_6, R.string.tab_text_7};
    private final Context mContext;

    public SectionsPagerAdapterMyPatientMF(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position)
        {
            case 0 :
                return new MyPatientHospitalisationsFragment();
            case 1 :
                return new MyPatientConsultationsFragment();
            default:
                return null;

        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}