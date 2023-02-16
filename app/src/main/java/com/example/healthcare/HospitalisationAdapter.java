package com.example.healthcare;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.healthcare.models.Hospitalisation;

import java.util.ArrayList;
import java.util.List;

public class HospitalisationAdapter extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<Hospitalisation> hospitalisationList;
    ArrayList<Hospitalisation> arrayList;


    public HospitalisationAdapter(Context context, List<Hospitalisation> hospitalisationList) {
        mContext = context;
        this.hospitalisationList = hospitalisationList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Hospitalisation>();
        this.arrayList.addAll(hospitalisationList);
    }

    @Override
    public int getCount() {
        return hospitalisationList.size();
    }

    @Override
    public Object getItem(int position) {
        return hospitalisationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.hospitalisation_row, null);
        final TextView hospitalName = convertView.findViewById(R.id.hospitalName);
        final TextView date = convertView.findViewById(R.id.date);
        TextView disease = convertView.findViewById(R.id.disease);
        TextView price = convertView.findViewById(R.id.price);

        final Hospitalisation hospitalisation = hospitalisationList.get(position);

        hospitalName.setText(hospitalisation.getHospitalName());
        date.setText(hospitalisation.getDate());
        disease.setText(hospitalisation.getDisease());
        price.setText(hospitalisation.getPrice());


        return convertView;
    }


}
