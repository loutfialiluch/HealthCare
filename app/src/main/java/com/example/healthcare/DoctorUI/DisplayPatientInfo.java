package com.example.healthcare.DoctorUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.healthcare.ImageResizer;
import com.example.healthcare.MyBounceInterpolator;
import com.example.healthcare.R;
import com.example.healthcare.models.Relationship;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

public class DisplayPatientInfo extends AppCompatActivity {
    private static int REQUEST_CALL = 1;
    FrameLayout frameLayout;
    CircleImageView circleImageView;
    TextView fullName, birthDate;
    ProgressBar progressBar;
    FloatingActionButton fab;
    String receivedFullName, receivedEmail, receivedBirthDate, receivedPhoneNumber, receivedCin, receivedMaritalStatus;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_patient_info);
        fullName = findViewById(R.id.fullName);
        birthDate = findViewById(R.id.birthDate);
        frameLayout = findViewById(R.id.myFrameLayout);
        circleImageView = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.myProgressBar);
        fab = findViewById(R.id.fab);

        Bitmap bMap = ImageResizer.decodeSampledBitmapFromResource(getResources(), R.drawable.background, 100, 100);
        BitmapDrawable dr = new BitmapDrawable(bMap);
        frameLayout.setBackground(dr);

        Intent intent = getIntent();
        receivedFullName = intent.getStringExtra("fullName");
        receivedEmail = intent.getStringExtra("email");
        receivedBirthDate = intent.getStringExtra("birthDate");
        receivedPhoneNumber = intent.getStringExtra("phoneNumber");
        receivedCin = intent.getStringExtra("cin");
        receivedMaritalStatus = intent.getStringExtra("maritalStatus");

        fullName.setText(receivedFullName);
        birthDate.setText(receivedBirthDate);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("Profile pictures").child(receivedEmail + ".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(circleImageView);
                progressBar.setVisibility(View.GONE);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Relationships");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Relationship relationship = data.getValue(Relationship.class);
                    if (relationship.getEmailPatient().equals(receivedEmail) && relationship.getEmailDoctor().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        fab.setVisibility(View.GONE);
                        SweetAlertDialog alertDialog = new SweetAlertDialog(DisplayPatientInfo.this,SweetAlertDialog.WARNING_TYPE);
                        alertDialog.setContentText(receivedFullName+" is one of your patients !");
                        alertDialog.show();
                        Button btn = alertDialog.findViewById(R.id.confirm_button);
                        btn.setBackgroundColor(Color.parseColor("#33aeb6"));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void phoneCall(View view) {
        ImageView phoneCallImage = findViewById(R.id.phoneCall);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        phoneCallImage.startAnimation(animation);
        makePhoneCall();
    }

    private void makePhoneCall() {
        if (ContextCompat.checkSelfPermission(DisplayPatientInfo.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DisplayPatientInfo.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        } else {
            String dial = "tel:" + receivedPhoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makePhoneCall();
            else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Gmail(View view) {
        ImageView gmailImage = findViewById(R.id.gmail);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        gmailImage.startAnimation(animation);
        sendMail();
    }

    private void sendMail() {
        String[] recipient = {receivedEmail};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    public void addPatient(View view) {
        Relationship relationship = new Relationship(FirebaseAuth.getInstance().getCurrentUser().getEmail(),receivedEmail);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Relationships");
        reference.push().setValue(relationship).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                new SweetAlertDialog(DisplayPatientInfo.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Done")
                        .setContentText(receivedFullName+" is added successfully to your list of patients !")
                        .show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                new SweetAlertDialog(DisplayPatientInfo.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Oops...")
                        .setContentText("Something went wrong!")
                        .show();
            }
        });
    }
}
