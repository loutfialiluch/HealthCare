package com.example.healthcare.DoctorUI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthcare.R;
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

public class DoctorDeclinedFragment extends Fragment {
    ListView declinedAppointments;
    DoctorAppointmentAdapter adapter;
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    List<Appointment> myAppointments;
    List<String> myReasons;
    List<String> myAppointmentsIds;
    FirebaseUser user;
    String emailDoctor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_declined_fragment, container, false);
        declinedAppointments = view.findViewById(R.id.declined_appointments);
        myAppointments = new ArrayList<>();
        myReasons = new ArrayList<>();
        myAppointmentsIds = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailDoctor = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAppointments.clear();
                myReasons.clear();
                myAppointmentsIds.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Appointment appointment = data.getValue(Appointment.class);
                    if (appointment.getEmailDoctor().equals(emailDoctor) && appointment.getStatus().equals("Declined")) {
                        myAppointmentsIds.add(data.getKey());
                        myReasons.add(data.child("reason").getValue(String.class));
                        myAppointments.add(appointment);
                        if (getActivity() != null) {
                            adapter = new DoctorAppointmentAdapter(getActivity(), myAppointments);
                            declinedAppointments.setAdapter(adapter);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        declinedAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditReasonDeclineDialog editReasonDeclineDialog = new EditReasonDeclineDialog(myAppointmentsIds.get(position), adapter, myReasons.get(position));
                editReasonDeclineDialog.show(getParentFragmentManager(), "reset password dialog");

            }
        });



        return view;
    }
}
