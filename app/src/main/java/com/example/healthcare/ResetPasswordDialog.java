package com.example.healthcare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ResetPasswordDialog extends AppCompatDialogFragment {
    private EditText email;
    private Button resetEmailButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        email = view.findViewById(R.id.email);
        resetEmailButton = view.findViewById(R.id.resetButton);
        resetEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(email.getText())) {
                    Toast.makeText(getActivity(), "Email field is empty", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        SweetAlertDialog alertDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                        alertDialog.setTitleText("Email sent successfully !");
                                        alertDialog.setContentText("Check your email box !");
                                        alertDialog.show();
                                        Button btn = alertDialog.findViewById(R.id.confirm_button);
                                        btn.setBackgroundColor(Color.parseColor("#33AEB6"));
                                        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                                startActivity(intent);
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
