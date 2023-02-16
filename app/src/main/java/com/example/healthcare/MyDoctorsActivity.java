package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.models.Doctor;
import com.example.healthcare.models.Relationship;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyDoctorsActivity extends AppCompatActivity {
    ListView myDoctorsListView;
    List<Doctor> Doctors;
    List<Doctor>  myDoctors;
    MyDoctorsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);
        myDoctorsListView = findViewById(R.id.myDoctors);
        Doctors = new ArrayList<>();
        myDoctors = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctors.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    Doctors.add(doctor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Relationships");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myDoctors.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Relationship relationship = data.getValue(Relationship.class);
                    for(int i=0; i<Doctors.size(); i++)
                    {
                        Doctor doc = Doctors.get(i);
                        if(relationship.getEmailDoctor().equals(doc.getEmail()) && relationship.getEmailPatient().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            myDoctors.add(doc);
                            adapter = new MyDoctorsAdapter(MyDoctorsActivity.this, myDoctors);
                            myDoctorsListView.setAdapter(adapter);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        myDoctorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyDoctorsActivity.this, MyDoctorInfoActivity.class);
                intent.putExtra("fullName",myDoctors.get(position).getFullName());
                intent.putExtra("email",myDoctors.get(position).getEmail());
                intent.putExtra("speciality",myDoctors.get(position).getSpeciality());
                intent.putExtra("phoneNumber",myDoctors.get(position).getPhoneNumber());
                intent.putExtra("address", myDoctors.get(position).getAddress());
                intent.putExtra("city", myDoctors.get(position).getCity());
                startActivity(intent);
            }
        });



    }
}
