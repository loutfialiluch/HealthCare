package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayItemDataActivity extends AppCompatActivity {

    TextView fullName, email, speciality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item_data);

        fullName = findViewById(R.id.userFullName);
        email = findViewById(R.id.emailAddress);
        speciality = findViewById(R.id.userSpeciality);

        Intent intent = getIntent();

        String receivedFullName = intent.getStringExtra("fullName");
        String receivedEmail = intent.getStringExtra("email");
        String receivedSpeciality = intent.getStringExtra("speciality");
        System.out.println(receivedEmail);

        fullName.setText(receivedFullName);
        email.setText(receivedEmail);
        speciality.setText(receivedSpeciality);


    }
}
