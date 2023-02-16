package com.example.healthcare.DoctorUI;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.healthcare.R;
import com.example.healthcare.models.Consultation;
import com.example.healthcare.models.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyPatientConsultationAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    List<Consultation> consultationList;
    ArrayList<Consultation> arrayList;
    DatabaseReference ref;


    public MyPatientConsultationAdapter(Context context, List<Consultation> consultationList) {
        mContext = context;
        this.consultationList = consultationList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Consultation>();
        this.arrayList.addAll(consultationList);
    }


    @Override
    public int getCount() {
        return consultationList.size();
    }

    @Override
    public Object getItem(int position) {
        return consultationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.mypatient_consultation_row, null);

        final CircleImageView doctorPicture = convertView.findViewById(R.id.profile_image);
        final TextView doctorFullName = convertView.findViewById(R.id.fullName);
        final TextView day = convertView.findViewById(R.id.day);
        final ImageView deleteView = convertView.findViewById(R.id.deleteView);
        final ImageView editView = convertView.findViewById(R.id.editView);
        editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final String[] consultationId = new String[1];
        final Consultation consultation = consultationList.get(position);
        final String emailDoctor = consultation.getDoctorEmail();
        final String emailPatient = consultation.getPatientEmail();
        ref = FirebaseDatabase.getInstance().getReference("Consultations");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                    Consultation consultation = data.getValue(Consultation.class);
                    if(consultation.getDoctorEmail().equals(emailDoctor) && consultation.getPatientEmail().equals(emailPatient))
                    {
                        consultationId[0] = data.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(final DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getEmail().equals(emailDoctor))
                    {
                        if(doctor.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                        {
                            editView.setImageResource(R.drawable.ic_edit);
                            editView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditConsultationDialog editConsultationDialog = new EditConsultationDialog(consultation, consultationId[0]);
                                    editConsultationDialog.show(((FragmentActivity)mContext).getSupportFragmentManager(), "edit consultation");
                                }
                            });
                            deleteView.setImageResource(R.drawable.ic_delete);
                            deleteView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Are you sure?")
                                            .setContentText("You won't be able to recover this consultation!")
                                            .setConfirmText("Yes, delete it!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    ref.child(consultationId[0]).removeValue();
                                                    notifyDataSetChanged();
                                                    sweetAlertDialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            });

                        }
                        doctorFullName.setText("Dr. "+doctor.getFullName());
                        day.setText(consultation.getDate());
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
