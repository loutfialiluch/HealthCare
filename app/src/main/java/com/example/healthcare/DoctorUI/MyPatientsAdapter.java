package com.example.healthcare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.healthcare.R;
import com.example.healthcare.models.Patient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class MyPatientsAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Patient> myPatientsList;
    ArrayList<Patient> arrayList;

    public MyPatientsAdapter(Context context, List<Patient> myPatientsList) {
        mContext = context;
        this.myPatientsList = myPatientsList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(myPatientsList);
    }
    @Override
    public int getCount() {
        return myPatientsList.size();
    }

    @Override
    public Object getItem(int position) {
        return myPatientsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.mypatient_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.profile_image);


        final TextView patientFullName = convertView.findViewById(R.id.fullName);
        final Patient patient = myPatientsList.get(position);
        final String emailPatient = patient.getEmail();

        patientFullName.setText(patient.getFullName());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("Profile pictures").child(emailPatient+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(doctorPicture);

            }
        });
        return convertView;
    }
}
