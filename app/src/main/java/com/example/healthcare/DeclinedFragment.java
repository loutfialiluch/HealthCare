package com.example.healthcare;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DeclinedFragment extends Fragment {
    ListView declinedAppointments;
    AppointmentAdapter adapter;
    ArrayList<Appointment> appointmentList = new ArrayList<>();
    List<Appointment> myAppointments;
    List <String> myReasons;
    FirebaseUser user;
    String emailPatient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.declined_fragment, container, false);
        declinedAppointments = view.findViewById(R.id.declined_appointments);
        myAppointments = new ArrayList<>();
        myReasons = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailPatient = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Appointments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myReasons.clear();
                myAppointments.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Appointment appointment = data.getValue(Appointment.class);
                    if(appointment.getEmailPatient().equals(emailPatient) && appointment.getStatus().equals("Declined")) {
                        myReasons.add(data.child("reason").getValue(String.class));
                        myAppointments.add(appointment);
                        if (getActivity()!=null){
                            adapter = new AppointmentAdapter(getActivity(), myAppointments);
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
                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE);
                alertDialog.setTitleText("Reason of refusal");
                alertDialog.setContentText(myReasons.get(position));
                alertDialog.show();
                Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                btn.setBackgroundColor(Color.parseColor("#33aeb6"));

            }
        });


        return view;
    }
}
