package com.example.healthcare;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.healthcare.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class SearchDoctorSpecialityActivity extends AppCompatActivity {
    int i;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor_speciality);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        searchView = findViewById(R.id.mySearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(i==1)
                {
                    if (TextUtils.isEmpty(s)) {
                        FragmentTwo.getAdapter().filter("");
                        FragmentTwo.getListView().clearTextFilter();
                    } else {
                        FragmentTwo.getAdapter().filter(s);
                    }

                }
                if(i==0) {
                    if (TextUtils.isEmpty(s)) {
                        FragmentOne.getAdapter().filter("");
                        FragmentOne.getListView().clearTextFilter();
                    } else {
                        FragmentOne.getAdapter().filter(s);
                    }
                }
                return true;
            }
        });
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                i = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}