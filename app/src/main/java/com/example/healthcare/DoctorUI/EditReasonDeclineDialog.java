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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditReasonDeclineDialog extends AppCompatDialogFragment {
    private EditText reason;
    private Button confirmButton, cancelButton;
    String oldReason;
    private String appointmentId;
    private DoctorAppointmentAdapter adapter;


    public EditReasonDeclineDialog(String appointmentId, DoctorAppointmentAdapter adapter, String oldReason)
    {
        this.appointmentId=appointmentId;
        this.adapter = adapter;
        this.oldReason = oldReason;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_reason_layout_dialog, null);
        reason = view.findViewById(R.id.reason);
        reason.setText(oldReason);
        confirmButton = view.findViewById(R.id.confirmButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(reason.getText())) {
                    Toast.makeText(getActivity(), "Reason field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    String reasonOfDecline = reason.getText().toString();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointments").child(appointmentId);
                    databaseReference.child("reason").setValue(reasonOfDecline).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                adapter.notifyDataSetChanged();
                                SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                alertDialog.setTitleText("Reason of refusal");
                                alertDialog.setContentText("Reason edited successfully !");
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
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);
        return builder.create();
    }
}
