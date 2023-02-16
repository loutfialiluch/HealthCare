package com.example.healthcare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.healthcare.ui.main.SectionsPagerAdapterMF;
import com.google.android.material.tabs.TabLayout;

public class MedicalFolderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_folder);
        SectionsPagerAdapterMF sectionsPagerAdapter = new SectionsPagerAdapterMF(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_ambulance);
        tabs.getTabAt(1).setIcon(R.drawable.ic_medical_history);
    }
}