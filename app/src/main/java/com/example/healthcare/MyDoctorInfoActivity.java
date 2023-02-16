package com.example.healthcare;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyDoctorInfoActivity extends AppCompatActivity {
    public static int REQUEST_CALL = 1;
    FrameLayout frameLayout;
    ImageView imageView;
    CircleImageView circleImageView;
    TextView fullName, speciality;
    String receivedEmail, receivedPhoneNumber, receivedAddress, receivedCity;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor_info);
        fullName = findViewById(R.id.fullName);
        speciality = findViewById(R.id.speciality);
        frameLayout = findViewById(R.id.myFrameLayout);
        imageView = findViewById(R.id.topside);
        circleImageView = findViewById(R.id.profile_image);
        progressBar = findViewById(R.id.myProgressBar);
        imageView.setImageBitmap(ImageResizer.decodeSampledBitmapFromResource(getResources(), R.drawable.topside, 250, 500));
        Bitmap bMap = ImageResizer.decodeSampledBitmapFromResource(getResources(), R.drawable.background, 100, 100);
        BitmapDrawable dr = new BitmapDrawable(bMap);
        frameLayout.setBackground(dr);


        Intent intent = getIntent();

        String receivedFullName = intent.getStringExtra("fullName");
        receivedEmail = intent.getStringExtra("email");
        String receivedSpeciality = intent.getStringExtra("speciality");
        receivedPhoneNumber = intent.getStringExtra("phoneNumber");
        receivedAddress = intent.getStringExtra("address");
        receivedCity = intent.getStringExtra("city");

        fullName.setText(receivedFullName);
        speciality.setText(receivedSpeciality);
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

    public void Gmail(View view) {
        ImageView gmailImage = findViewById(R.id.gmail);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        gmailImage.startAnimation(animation);
        sendMail();
    }


    public void Locate(View view) {
        ImageView localisationImage = findViewById(R.id.localisation);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        animation.setInterpolator(interpolator);
        localisationImage.startAnimation(animation);
        Intent intent = new Intent(MyDoctorInfoActivity.this, DoctorLocalisation.class);
        intent.putExtra("address", receivedAddress);
        intent.putExtra("city",receivedCity);
        startActivity(intent);
    }

    private void sendMail() {
        String[] recipient = {receivedEmail};

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email client"));
    }

    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(MyDoctorInfoActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(MyDoctorInfoActivity.this, new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);

        }
        else
        {
            String dial = "tel:"+receivedPhoneNumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                makePhoneCall();
            else
            {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void scheduleAppointment(View view) {
        Intent intent = new Intent(MyDoctorInfoActivity.this, ScheduleAppointmentActivity.class);
        intent.putExtra("email",receivedEmail);
        startActivity(intent);
    }
}
