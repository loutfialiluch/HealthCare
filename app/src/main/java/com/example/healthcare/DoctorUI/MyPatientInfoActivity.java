package com.example.healthcare.DoctorUI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyPatientInfoActivity extends AppCompatActivity {
    private static int REQUEST_CALL = 1;
    FrameLayout frameLayout;
    CircleImageView circleImageView;
    TextView fullName, birthDate;
    ProgressBar progressBar;
    String receivedFullName, receivedEmail, receivedBirthDate, receivedPhoneNumber, receivedCin, receivedMaritalStatus, relationshipId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patient_info);
        fullName = findViewById(R.id.fullName);
        birthDate = findViewById(R.id.birthDate);
        frameLayout = findViewById(R.id.myFrameLayout);
        circleImageView = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.myProgressBar);

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
        relationshipId = intent.getStringExtra("relationshipId");

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
        if (ContextCompat.checkSelfPermission(MyPatientInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MyPatientInfoActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);

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

    public void displayMF(View view) {
        ImageView medicalFolder = findViewById(R.id.medicalFolder);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        medicalFolder.startAnimation(animation);
        Intent intent = new Intent(MyPatientInfoActivity.this, MyPatientMedicalFolderActivity.class);
        MyPatientConsultationsFragment.setEmailPatient(receivedEmail);
        MyPatientHospitalisationsFragment.setEmailPatient(receivedEmail);
        startActivity(intent);

    }

    public void deletePatient(View view) {
        new SweetAlertDialog(MyPatientInfoActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Patient deletion")
                .setContentText("You really want to delete "+receivedFullName+" from your list of patients ?")
                .setConfirmText("Yes, I do !")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Relationships");
                        ref.child(relationshipId).removeValue();
                        MyPatientsActivity.notifyAdapter();
                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(MyPatientInfoActivity.this, MyPatientsActivity.class);
                        startActivity(intent);
                    }
                })
                .show();

    }
}
