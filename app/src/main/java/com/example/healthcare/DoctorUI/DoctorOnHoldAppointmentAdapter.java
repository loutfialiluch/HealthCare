package com.example.healthcare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.healthcare.R;
import com.example.healthcare.models.Appointment;
import com.example.healthcare.models.Patient;
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

public class DoctorOnHoldAppointmentAdapter extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Appointment> appointmentList;
    ArrayList<Appointment> arrayList;
    String emailPatient;
    String appointmentId;


    public DoctorOnHoldAppointmentAdapter(Context context, List<Appointment> appointmentList) {
        mContext = context;
        this.appointmentList = appointmentList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<>();
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
        convertView = inflater.inflate(R.layout.onhold_appointment_row, null);

        final CircleImageView patientPicture = convertView.findViewById(R.id.profile_image);


        final TextView patientFullName = convertView.findViewById(R.id.fullName);
        final TextView day = convertView.findViewById(R.id.day);
        final TextView time = convertView.findViewById(R.id.time);
        Button acceptButton = convertView.findViewById(R.id.acceptButton);
        Button declineButton = convertView.findViewById(R.id.declineButton);

        final Appointment appointment = appointmentList.get(position);
        appointmentId = "";
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Appointments");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Appointment appt = data.getValue(Appointment.class);
                    if(appt.getDate().equals(appointment.getDate()) && appt.getTime().equals(appointment.getTime())) {
                        appointmentId = data.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
                databaseReference.child("status").setValue("Accepted");
                notifyDataSetChanged();
            }
        });

        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReasonDeclineDialog reasonDeclineDialog = new ReasonDeclineDialog(appointmentId, DoctorOnHoldAppointmentAdapter.this);
                reasonDeclineDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(), "reason of refusal");
            }
        });
        emailPatient = appointment.getEmailDoctor();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patients");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Patient patient = data.getValue(Patient.class);
                    if (patient.getEmail().equals(appointment.getEmailPatient())) {
                        patientFullName.setText(patient.getFullName());
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
        final StorageReference profileRef = storageReference.child("Profile pictures").child(emailPatient + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mContext).load(uri).into(patientPicture);

            }
        });
        return convertView;
    }


}
