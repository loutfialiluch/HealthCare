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

import com.example.healthcare.HospitalisationAdapter;
import com.example.healthcare.R;
import com.example.healthcare.models.Hospitalisation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class MyPatientHospitalisationsFragment extends Fragment {
    FloatingActionButton fab;
    ListView hospitalisations;
    HospitalisationAdapter adapter;
    ArrayList<Hospitalisation> hospitalisationList = new ArrayList<>();
    List<Hospitalisation> myHospitalisations;
    List<String> myHospitalisationsId;
    FirebaseUser user;
    static String emailPatient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypatient_hospitalisation_fragment, container, false);
        hospitalisations = view.findViewById(R.id.hospitalisations);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddHospitalisationDialog  addHospitalisationDialog = new AddHospitalisationDialog(emailPatient);
                addHospitalisationDialog.show(getFragmentManager(), "add hospitalisation dialog");
            }
        });
        myHospitalisations = new ArrayList<>();
        myHospitalisationsId = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hospitalisations");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myHospitalisations.clear();
                myHospitalisationsId.clear();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Hospitalisation hospitalisation = data.getValue(Hospitalisation.class);
                    if(hospitalisation.getEmailPatient().equals(emailPatient)) {
                        myHospitalisations.add(hospitalisation);
                        Collections.sort(myHospitalisations);
                        myHospitalisationsId.add(data.getKey());
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
        hospitalisations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EditHospitalisationDialog editHospitalisationDialog = new EditHospitalisationDialog(myHospitalisations.get(position), myHospitalisationsId.get(position));
                editHospitalisationDialog.show(getFragmentManager(), "edit hospitalisation");
            }
        });
        return view;
    }
    public static void setEmailPatient(String email)
    {
        emailPatient = email;
    }
}
