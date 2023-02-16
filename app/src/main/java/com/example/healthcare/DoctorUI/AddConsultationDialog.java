package com.example.healthcare.DoctorUI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.healthcare.R;
import com.example.healthcare.models.Consultation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddConsultationDialog extends AppCompatDialogFragment {
    private EditText disease, price, date, prescription;
    private Button addConsultationButton;
    private String doctorName, doctorEmail, patientEmail;


    public AddConsultationDialog(String doctorName, String doctorEmail, String patientEmail)
    {
        this.doctorName = doctorName;
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_consultation_dialog, null);
        disease = view.findViewById(R.id.disease);
        price = view.findViewById(R.id.price);
        date = view.findViewById(R.id.date);
        prescription = view.findViewById(R.id.prescription);
        addConsultationButton = view.findViewById(R.id.addConsultationButton);
        addConsultationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(prescription.getText()) || TextUtils.isEmpty(disease.getText()) || TextUtils.isEmpty(price.getText())) {
                    Toast.makeText(getActivity(), "A field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Consultation consultation = new Consultation(doctorName, doctorEmail, patientEmail, disease.getText().toString(), date.getText().toString(),
                            price.getText().toString()+" DH", prescription.getText().toString());
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Consultations");
                    ref.push().setValue(consultation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Add consultation");
                                alertDialog.setContentText("Consultation added successfully !");
                                alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    });


                }

            }
        });
        builder.setView(view);
        return builder.create();
    }
}
