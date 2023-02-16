package com.example.healthcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthcare.models.Appointment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptedFragment extends Fragment {
    ListView acceptedAppointments;
    AppointmentAdapter adapter;
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    List<Appointment> myAppointments;
    FirebaseUser user;
    String emailPatient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accepted_fragment, container, false);
        acceptedAppointments = view.findViewById(R.id.accepted_appointments);
        myAppointments = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailPatient = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAppointments.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Appointment appointment = data.getValue(Appointment.class);
                    if(appointment.getEmailPatient().equals(emailPatient) && appointment.getStatus().equals("Accepted")) {
                        myAppointments.add(appointment);
                        if (getActivity()!=null){
                            adapter = new AppointmentAdapter(getActivity(), myAppointments);
                            acceptedAppointments.setAdapter(adapter);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        return view;
    }
}
