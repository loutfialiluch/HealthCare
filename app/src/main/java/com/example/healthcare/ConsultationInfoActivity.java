package com.example.healthcare;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.models.Doctor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConsultationInfoActivity extends AppCompatActivity {
    CircleImageView profilePicture;
    TextView fullName, speciality, date, price, disease;
    String receivedPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultation_info);
        profilePicture = findViewById(R.id.profile_image);
        fullName = findViewById(R.id.fullName);
        speciality = findViewById(R.id.speciality);
        date = findViewById(R.id.date);
        price = findViewById(R.id.price);
        disease = findViewById(R.id.disease);
        Intent intent = getIntent();
        String receivedFullName = intent.getStringExtra("doctorName");
        final String receivedDoctorEmail = intent.getStringExtra("doctorEmail");
        String receivedDate = intent.getStringExtra("date");
        String receivedPrice = intent.getStringExtra("price");
        String receivedDisease = intent.getStringExtra("disease");
        receivedPrescription = intent.getStringExtra("prescription");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("Profile pictures").child(receivedDoctorEmail + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicture);
            }
        });

        fullName.setText(receivedFullName);
        date.setText(receivedDate);
        price.setText(receivedPrice);
        disease.setText(receivedDisease);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Doctors");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    Doctor doctor = data.getValue(Doctor.class);
                    if(doctor.getEmail().equals(receivedDoctorEmail)) {
                        speciality.setText(doctor.getSpeciality());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void prescription(View view) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        sweetAlertDialog.setTitleText("Prescription");
        sweetAlertDialog.setContentText(receivedPrescription);
        sweetAlertDialog.show();
        Button button = sweetAlertDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM);
        button.setBackgroundColor(Color.parseColor("#33aeb6"));

    }
}
