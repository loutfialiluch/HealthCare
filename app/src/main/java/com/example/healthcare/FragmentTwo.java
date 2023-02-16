package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FragmentTwo extends Fragment {
    static ListView listView;
    static SpecialityAdapter adapter;
    ArrayList<String> specialityList;
    static SpecialityAdapter getAdapter(){
        return adapter;
    }
    public static ListView getListView(){return listView;}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenttwo_layout, container, false);
        listView = view.findViewById(R.id.specialityList);
        specialityList = new ArrayList<>();
        specialityList.addAll(Arrays.asList(getResources().getStringArray(R.array.doctorSpeciality)));
        Collections.sort(specialityList);
        adapter = new SpecialityAdapter(getContext(),specialityList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DoctorsBySpecialityActivity.class);
                intent.putExtra("speciality", specialityList.get(position));
                startActivity(intent);
            }
        });
        return view;
    }
}
