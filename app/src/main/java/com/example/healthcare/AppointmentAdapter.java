package com.example.healthcare;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.example.healthcare.models.Appointment;
import com.example.healthcare.models.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AppointmentAdapter extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Appointment> appointmentList;
    ArrayList<Appointment> arrayList;
    String emailDoctor, emailPatient;


    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        mContext = context;
        this.appointmentList = appointmentList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Appointment>();
        this.arrayList.addAll(appointmentList);
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.appointment_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.profile_image);


        final TextView doctorFullName = convertView.findViewById(R.id.fullName);
        final TextView day = convertView.findViewById(R.id.day);
        final TextView time = convertView.findViewById(R.id.time);


        final Appointment appointment = appointmentList.get(position);
        emailDoctor = appointment.getEmailDoctor();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getEmail().equals(appointment.getEmailDoctor()))
                    {
                        doctorFullName.setText("Dr. "+doctor.getFullName());
                        day.setText(appointment.getDate());
                        time.setText(appointment.getTime());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

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
