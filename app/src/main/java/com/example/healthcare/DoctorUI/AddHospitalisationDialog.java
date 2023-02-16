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
import com.example.healthcare.models.Hospitalisation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddHospitalisationDialog extends AppCompatDialogFragment {
    private EditText hospitalName, disease, price, date;
    private Button addHospitalisationButton;
    private String patientEmail;


    public AddHospitalisationDialog(String patientEmail)
    {
        this.patientEmail = patientEmail;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_hospitalisation_dialog, null);
        hospitalName = view.findViewById(R.id.hospitalName);
        disease = view.findViewById(R.id.disease);
        price = view.findViewById(R.id.price);
        date = view.findViewById(R.id.date);
        addHospitalisationButton = view.findViewById(R.id.addHospitalisationButton);
        addHospitalisationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(hospitalName.getText()) || TextUtils.isEmpty(disease.getText()) || TextUtils.isEmpty(price.getText()) || TextUtils.isEmpty(date.getText())) {
                    Toast.makeText(getActivity(), "A field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Hospitalisation hospitalisation = new Hospitalisation(hospitalName.getText().toString(), patientEmail, date.getText().toString(),
                     disease.getText().toString(), price.getText().toString()+" DH");
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Hospitalisations");
                    ref.push().setValue(hospitalisation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Add hospitalisation");
                                alertDialog.setContentText("Hospitalisation added successfully !");
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
