package com.example.healthcare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthcare.models.Hospitalisation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HospitalisationFragment extends Fragment {
    ListView hospitalisations;
    HospitalisationAdapter adapter;
    ArrayList<Hospitalisation> hospitalisationList = new ArrayList<>();
    List<Hospitalisation> myHospitalisations;
    FirebaseUser user;
    String emailPatient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hospitalisation_fragment, container, false);
        hospitalisations = view.findViewById(R.id.hospitalisations);
        myHospitalisations = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        emailPatient = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hospitalisations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myHospitalisations.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Hospitalisation hospitalisation = data.getValue(Hospitalisation.class);
                    if(hospitalisation.getEmailPatient().equals(emailPatient)) {
                        myHospitalisations.add(hospitalisation);
                        Collections.sort(myHospitalisations);
                        if (getActivity()!=null){
                            adapter = new HospitalisationAdapter(getActivity(), myHospitalisations);
                            hospitalisations.setAdapter(adapter);
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
