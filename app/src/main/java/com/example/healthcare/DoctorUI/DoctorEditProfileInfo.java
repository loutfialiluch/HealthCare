package com.example.healthcare.DoctorUI;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.R;
import com.example.healthcare.RevealAnimation;
import com.example.healthcare.models.Doctor;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorEditProfileInfo extends AppCompatActivity {
    private static final int Pick_Image_Request = 1;
    RevealAnimation mRevealAnimation;
    CircleImageView circleImageView;
    private Uri mImageUri;
    EditText fullName, speciality, email, phoneNumber, address, city;
    String receivedFullName, receivedSpeciality, receivedEmail, receivedPhoneNumber, receivedAddress, receivedCity, receivedCode;
    String receivedImageUri;
    StorageReference mStorageReference;
    FirebaseDatabase database;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile_info);

        fullName = findViewById(R.id.fullName);
        speciality = findViewById(R.id.speciality);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.phoneNumber);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        circleImageView = findViewById(R.id.profile_image);

        mStorageReference = FirebaseStorage.getInstance().getReference("Profile pictures");

        Intent intent = this.getIntent();   //get the intent to recieve the x and y coords, that you passed before

        receivedFullName = intent.getStringExtra("fullName");
        receivedSpeciality = intent.getStringExtra("speciality");
        receivedEmail = intent.getStringExtra("email");
        receivedPhoneNumber = intent.getStringExtra("phoneNumber");
        receivedAddress = intent.getStringExtra("address");
        receivedCity = intent.getStringExtra("city");
        receivedCode = intent.getStringExtra("code");
        receivedImageUri = intent.getStringExtra("imageUri");
        Uri uri = Uri.parse(receivedImageUri);

        fullName.setText(receivedFullName);
        speciality.setText(receivedSpeciality);
        email.setText(receivedEmail);
        phoneNumber.setText(receivedPhoneNumber);
        address.setText(receivedAddress);
        city.setText(receivedCity);
        Picasso.get().load(uri).into(circleImageView);

        FrameLayout rootLayout = findViewById(R.id.root); //there you have to get the root layout of your second activity
        mRevealAnimation = new RevealAnimation(rootLayout, intent, this);
    }
    public void onBackPressed() {
        mRevealAnimation.unRevealActivity();
    }

    public void editProfilePicture(View view) {
        openFileChooser();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Pick_Image_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Pick_Image_Request && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(circleImageView);
            uploadImage();
        }

    }

    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadImage() {
        StorageReference ref = mStorageReference.child(receivedEmail + "." + getExtension(mImageUri));
        ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(DoctorEditProfileInfo.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void update(View view) {
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String userUid = user.getUid();
        DatabaseReference dbRef = database.getReference("Doctors");
        final Doctor doctor = new Doctor(fullName.getText().toString(),receivedCode,phoneNumber.getText().toString()
                ,email.getText().toString(),city.getText().toString(),address.getText().toString(), receivedSpeciality);
        dbRef.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(doctor);
                Intent intent = new Intent(DoctorEditProfileInfo.this, DisplayDoctorProfileInfo.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
