package com.example.healthcare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.DoctorUI.DoctorMenuActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText login;
    private EditText password;
    private TextView errorMessage;
    CheckBox rememberMe;
    SharedPreferences sp;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        login = findViewById(R.id.login);
        password = findViewById(R.id.password);
        errorMessage = findViewById(R.id.errorMessage);
        rememberMe = findViewById(R.id.rememberMe);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (sp.getBoolean("loggedPatient", false)) {
            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(intent);
        }
        if(sp.getBoolean("loggedDoctor", false))
        {
            Intent intent = new Intent(MainActivity.this, DoctorMenuActivity.class);
            startActivity(intent);
        }

    }

    public void createAccount(View view) {
        Intent createAccountIntent = new Intent(this, CreateAccount.class);
        startActivity(createAccountIntent);
    }


    public void login(View view) {
        errorMessage.setVisibility(View.GONE);
        String tempLogin = login.getText().toString().trim();
        String tempPassword = password.getText().toString().trim();
        if (
                TextUtils.isEmpty(tempLogin)
                        || TextUtils.isEmpty(tempPassword)
        ) {
            Toast.makeText(MainActivity.this, "Login or Password are empty", Toast.LENGTH_SHORT).show();
        } else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#33aeb6"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            mAuth.signInWithEmailAndPassword(tempLogin, tempPassword).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        ref = FirebaseDatabase.getInstance().getReference("Doctors");
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String email = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("email").getValue(String.class);
                                if (email == null) {
                                    pDialog.hide();
                                    if (rememberMe.isChecked()) {
                                        sp.edit().putBoolean("loggedPatient", true).apply();
                                    } else
                                        sp.edit().putBoolean("loggedPatient", false).apply();
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    MainActivity.this.startActivity(intent);
                                } else {
                                    pDialog.hide();
                                    if (rememberMe.isChecked()) {
                                        sp.edit().putBoolean("loggedDoctor", true).apply();
                                    } else
                                        sp.edit().putBoolean("loggedDoctor", false).apply();
                                    Intent intent = new Intent(MainActivity.this, DoctorMenuActivity.class);
                                    MainActivity.this.startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        errorMessage.setVisibility(View.VISIBLE);
                        pDialog.hide();
                    }
                }
            });
        }
    }

    public void passwordForgotten(View view) {
        ResetPasswordDialog resetPasswordDialog = new ResetPasswordDialog();
        resetPasswordDialog.show(getSupportFragmentManager(), "reset password dialog");

    }
}
