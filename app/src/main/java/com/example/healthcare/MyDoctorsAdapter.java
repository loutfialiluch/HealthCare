package com.example.healthcare;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.healthcare.models.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDoctorsAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Doctor> myDoctorsList;
    ArrayList<Doctor> arrayList;


    public MyDoctorsAdapter(Context context, List<Doctor> myDoctorsList) {
        mContext = context;
        this.myDoctorsList = myDoctorsList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(myDoctorsList);
    }

    @Override
    public int getCount() {
        return myDoctorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return myDoctorsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.mydoctor_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.profile_image);


        final TextView doctorFullName = convertView.findViewById(R.id.fullName);
        final TextView speciality = convertView.findViewById(R.id.speciality);
        final Doctor doctor = myDoctorsList.get(position);
        final String emailDoctor = doctor.getEmail();

        doctorFullName.setText("Dr. "+doctor.getFullName());
        speciality.setText(doctor.getSpeciality());
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference profileRef = storageReference.child("Profile pictures").child(emailDoctor+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(doctorPicture);

            }
        });
    return convertView;
    }
    }