package com.example.healthcare.DoctorUI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.healthcare.R;
import com.example.healthcare.RevealAnimation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DisplayDoctorProfileInfo extends AppCompatActivity {
    TextView fullName, speciality, email, phoneNumber, address;
    CircleImageView circleImageView;
    FirebaseUser user;
    String uid;
    String Uri;
    DatabaseReference databaseReference;
    String fullNameRetrieved, specialityRetrieved, emailRetrieved, phoneNumberRetrieved, addressRetrieved, cityRetrieved, codeRetrieved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_doctor_profile_info);
        fullName = findViewById(R.id.fullName);
        speciality = findViewById(R.id.speciality);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.fullAddress);
        circleImageView = findViewById(R.id.profile_image);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Doctors");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                fullNameRetrieved =  dataSnapshot.child(uid).child("fullName").getValue(String.class);
                fullName.setText(fullNameRetrieved);
                specialityRetrieved = dataSnapshot.child(uid).child("speciality").getValue(String.class);
                speciality.setText(specialityRetrieved);
                emailRetrieved = dataSnapshot.child(uid).child("email").getValue(String.class);
                email.setText(emailRetrieved);
                phoneNumberRetrieved = dataSnapshot.child(uid).child("phoneNumber").getValue(String.class);
                phoneNumber.setText(phoneNumberRetrieved);
                addressRetrieved = dataSnapshot.child(uid).child("address").getValue(String.class);
                cityRetrieved = dataSnapshot.child(uid).child("city").getValue(String.class);
                address.setText(addressRetrieved+", "+cityRetrieved);

                codeRetrieved = dataSnapshot.child(uid).child("code").getValue(String.class);

                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference profileRef = storageReference.child("Profile pictures").child(emailRetrieved + ".jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<android.net.Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri = uri.toString();
                        Picasso.get().load(uri).into(circleImageView);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startRevealActivity(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, DoctorEditProfileInfo.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("fullName", fullNameRetrieved);
        intent.putExtra("speciality", specialityRetrieved);
        intent.putExtra("email", emailRetrieved);
        intent.putExtra("phoneNumber", phoneNumberRetrieved);
        intent.putExtra("address", addressRetrieved);
        intent.putExtra("city", cityRetrieved);
        intent.putExtra("code", codeRetrieved);
        intent.putExtra("imageUri", Uri);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    public void editProfile(View view) {
        startRevealActivity(view);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DisplayDoctorProfileInfo.this, DoctorMenuActivity.class);
        startActivity(intent);
    }
}
