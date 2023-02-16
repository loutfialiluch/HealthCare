package com.example.healthcare.DoctorUI;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.R;
import com.example.healthcare.models.Patient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPatientActivity extends AppCompatActivity {
    ListView listView;
    SearchPatientAdapter adapter;
    List<Patient> myPatients;
    SearchView  searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_patient);
        listView = findViewById(R.id.patients);
        searchView = findViewById(R.id.mySearchView);
        myPatients = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPatients.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Patient patient = data.getValue(Patient.class);
                    myPatients.add(patient);
                    Collections.sort(myPatients);
                    adapter = new SearchPatientAdapter(SearchPatientActivity.this, myPatients);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                    if (TextUtils.isEmpty(s)) {
                        adapter.filter("");
                        listView.clearTextFilter();
                    } else {
                        adapter.filter(s);
                    }
                return true;
        }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchPatientActivity.this, DisplayPatientInfo.class);
                intent.putExtra("fullName",myPatients.get(position).getFullName());
                intent.putExtra("email",myPatients.get(position).getEmail());
                intent.putExtra("birthDate",myPatients.get(position).getBirthDate());
                intent.putExtra("phoneNumber",myPatients.get(position).getPhoneNumber());
                intent.putExtra("cin", myPatients.get(position).getCin());
                intent.putExtra("maritalStatus", myPatients.get(position).getMaritalStatus());
                startActivity(intent);
            }
        });
    }
}
