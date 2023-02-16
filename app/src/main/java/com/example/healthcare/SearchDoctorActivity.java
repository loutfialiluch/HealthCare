package com.example.healthcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcare.models.Doctor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchDoctorActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Doctor> myDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_doctor);
        recyclerView= (RecyclerView)findViewById(R.id.myRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myDoctors = new ArrayList<>();
        recyclerView.setAdapter(new doctorAdapters(myDoctors));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    myDoctors.add(doctor);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
    class doctorAdapters extends RecyclerView.Adapter<DoctorViewHolder>{
        List<Doctor> myDoctors;
        public doctorAdapters(List<Doctor> myDoctors) {
            super();
            this.myDoctors=myDoctors;
        }
        @NonNull
        @Override
        public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DoctorViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
            holder.bind(this.myDoctors.get(position));

        }

        @Override
        public int getItemCount() {
            return myDoctors.size();
        }
    }
    class DoctorViewHolder extends RecyclerView.ViewHolder{
        private TextView fullName;
        private TextView emailAddress;
        public DoctorViewHolder(ViewGroup container)
        {
            super(LayoutInflater.from(SearchDoctorActivity.this).inflate(R.layout.item_layout, container, false));
            fullName=(TextView)itemView.findViewById(R.id.fullName);
            emailAddress=(TextView)itemView.findViewById(R.id.emailAddress);
        }
        public void bind(Doctor doctor)
        {
            fullName.setText(doctor.getFullName());
            emailAddress.setText(doctor.getEmail());
        }
    }
}

