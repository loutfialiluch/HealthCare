package com.example.healthcare.DoctorUI;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.healthcare.R;
import com.example.healthcare.models.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchPatientAdapter extends BaseAdapter {
    int i=0;
    Context mContext;
    LayoutInflater inflater;
    List<Patient> patientList;
    ArrayList<Patient> arrayList;

    public SearchPatientAdapter(Context context, List<Patient> patientList) {
        mContext = context;
        this.patientList = patientList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList();
        this.arrayList.addAll(patientList);
    }

    @Override
    public int getCount() {
        return patientList.size();
    }

    @Override
    public Object getItem(int position) {
        return patientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.row,null);

        TextView circleText = convertView.findViewById(R.id.circle_text);
        TextView fullName = convertView.findViewById(R.id.doctorFullName);

        Patient patient = patientList.get(position);

        fullName.setText(patient.getFullName().trim());
        circleText.setText((patient.getFullName().charAt(0)+"").toUpperCase());
        return convertView;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        patientList.clear();
        if (charText.length()==0){
            patientList.addAll(arrayList);
        }
        else {
            for (Patient p : arrayList){
                if (p.getFullName().toLowerCase(Locale.getDefault())
                        .contains(charText)){
                    patientList.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

}
