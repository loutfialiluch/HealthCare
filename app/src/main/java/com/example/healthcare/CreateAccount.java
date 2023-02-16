package com.example.healthcare;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.models.Doctor;
import com.example.healthcare.models.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateAccount extends AppCompatActivity {
    //Doctor fields
    LinearLayout doctorLinearLayout;
    RadioButton doctorRadioButton;
    EditText doctorFullName, doctorCode, doctorPhoneNumber, doctorEmail, doctorCity, doctorAddress, doctorSpeciality, doctorPassword1, doctorPassword2;
    Button doctorButton;
    // Patient fields
    LinearLayout patientLinearLayout;
    RadioButton patientRadioButton;
    EditText patientFirstName, patientLastName, patientBirthDate, patientPhoneNumber, patientEmail, patientCIN, patientPassword1, patientPassword2;
    Spinner maritalStatus;
    Button patientButton;
    //Loading screen
    loadingDialog ld;
    //Date picker
    DatePickerDialog.OnDateSetListener DateSetListener;

    private FirebaseAuth fbAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Loading dialog
         ld = new loadingDialog(CreateAccount.this);

        // Patient fields
        patientFirstName = findViewById(R.id.patientFirstName);
        patientLastName = findViewById(R.id.patientLastName);
        patientBirthDate = findViewById(R.id.patientBirthDate);
        patientPhoneNumber = findViewById(R.id.patientPhoneNumber);
        patientEmail = findViewById(R.id.patientEmail);
        patientCIN = findViewById(R.id.patientCIN);
        patientPassword1 = findViewById(R.id.patientPassword1);
        patientPassword2 = findViewById(R.id.patientPassword2);
        patientRadioButton=(RadioButton)findViewById(R.id.patientRadioButton);
        patientLinearLayout=(LinearLayout)findViewById(R.id.patientLinearLayout);
        maritalStatus = (Spinner) findViewById(R.id.maritalStatus);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.marital_status, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maritalStatus.setAdapter(adapter);
        patientButton = findViewById(R.id.patientButton);

        // Doctor fields
        doctorRadioButton=(RadioButton)findViewById(R.id.doctorRadioButton);
        doctorLinearLayout=(LinearLayout)findViewById(R.id.doctorLinearLayout);
        doctorFullName = findViewById(R.id.doctorFullName);
        doctorCode = findViewById(R.id.Code);
        doctorPhoneNumber = findViewById(R.id.doctorPhoneNumber);
        doctorEmail = findViewById(R.id.doctorEmail);
        doctorCity = findViewById(R.id.City);
        doctorAddress = findViewById(R.id.Address);
        doctorSpeciality = findViewById(R.id.Speciality);
        doctorPassword1 = findViewById(R.id.doctorPassword1);
        doctorPassword2 = findViewById(R.id.doctorPassword2);
        doctorButton = findViewById(R.id.doctorButton);



        // Initializing the firstbase object
        fbAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fbAuth.getCurrentUser() != null)
        {
            //handle the already connected user
        }
    }

    public void patientRegistration(View view) {
        if(patientRadioButton.isChecked())
        {
            doctorLinearLayout.setVisibility(View.GONE);
            patientLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void doctorRegistration(View view) {
        if(doctorRadioButton.isChecked())
        {
            patientLinearLayout.setVisibility(View.GONE);
            doctorLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void registerPatient() {
        final String firstName = patientFirstName.getText().toString().trim();
        final String lastName = patientLastName.getText().toString().trim();
        final String birthDate = patientBirthDate.getText().toString().trim();
        final String phoneNumber = patientPhoneNumber.getText().toString().trim();
        final String email = patientEmail.getText().toString().trim();
        final String CIN = patientCIN.getText().toString().trim();
        final String status = maritalStatus.getSelectedItem().toString().trim();
        String password = patientPassword1.getText().toString().trim();
        String password2 = patientPassword2.getText().toString().trim();
        if(
                TextUtils.isEmpty(firstName)
                || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(birthDate)
                || TextUtils.isEmpty(phoneNumber)
                || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(CIN)
                || TextUtils.isEmpty(status)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(password2)
        )
        {
            Toast.makeText(CreateAccount.this, "All fields are required !", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!isEmailValid(email)) {
                patientEmail.setError("Invalid email format !");
                return;
            }
            if(!password.equals(password2))
            {
                patientPassword1.setError("The two passwords are not matched");
                return;
            }
            else
            {
                if(password.length()<=3) {
                    patientPassword1.setError("Password must be longer than three characters");
                    return;
                }

            }
            if(!isDateValid(birthDate))
            {
                patientBirthDate.setError("Date should match the YYYY-MM-DD format !");
                return;
            }
            ld.startLoadingDialog();
            fbAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Patient patient = new Patient(firstName, lastName, birthDate, phoneNumber, email, CIN, status);
                        FirebaseDatabase.getInstance().getReference("Patients")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(patient).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(CreateAccount.this, "User created successfully", Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                                else {
                                    Toast.makeText(CreateAccount.this, "User creation failed", Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                            }
                        });
                    }
                    else {
                        ld.dismissDialog();
                        Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });


        }




        }

    private void registerDoctor() {

        final String fullName = doctorFullName.getText().toString().trim();
        final String Code = doctorCode.getText().toString().trim();
        final String phoneNumber = doctorPhoneNumber.getText().toString().trim();
        final String Email = doctorEmail.getText().toString().trim();
        final String City = doctorCity.getText().toString().trim();
        final String Address = doctorAddress.getText().toString().trim();
        final String Speciality = doctorSpeciality.getText().toString().trim();
        String password1 = doctorPassword1.getText().toString().trim();
        String password2 = doctorPassword2.getText().toString().trim();
        if(
                TextUtils.isEmpty(fullName)
                        || TextUtils.isEmpty(Code)
                        || TextUtils.isEmpty(phoneNumber)
                        || TextUtils.isEmpty(Email)
                        || TextUtils.isEmpty(City)
                        || TextUtils.isEmpty(Address)
                        || TextUtils.isEmpty(Speciality)
                        || TextUtils.isEmpty(password1)
                        || TextUtils.isEmpty(password2)
        )
        {
            Toast.makeText(CreateAccount.this, "All fields are required !", Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!isEmailValid(Email)) {
                patientEmail.setError("Invalid email format !");
                return;
            }
            if(!password1.equals(password2))
            {
                patientPassword1.setError("The two passwords are not matched");
                return;
            }
            else
            {
                if(password1.length()<=3) {
                    patientPassword1.setError("Password must be longer than three characters");
                    return;
                }

            }
            ld.startLoadingDialog();
            fbAuth.createUserWithEmailAndPassword(Email,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Doctor doctor = new Doctor(fullName, Code, phoneNumber, Email, City, Address, Speciality);
                        FirebaseDatabase.getInstance().getReference("Doctors")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(doctor).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Toast.makeText(CreateAccount.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                                else {
                                    Toast.makeText(CreateAccount.this, "Registration failed", Toast.LENGTH_LONG).show();
                                    ld.dismissDialog();
                                }
                            }
                        });
                    }
                    else {
                        ld.dismissDialog();
                        Toast.makeText(CreateAccount.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }


    public void signUpPatient(View view) {
        registerPatient();
    }

    public void signUpDoctor(View view) {
        registerDoctor();
    }



    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public static boolean isDateValid(String date)
    {
        String expression = "^(19|20)\\d\\d[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();

    }
}
