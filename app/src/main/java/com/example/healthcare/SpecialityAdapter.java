package com.example.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SpecialityAdapter extends BaseAdapter {
    int i = 0;
    Context mContext;
    LayoutInflater inflater;
    List<String> specialityList;
    ArrayList<String> arrayList;
    String[] Colors = {"#9E9E9E", "#039BE5", "#00C853", "#C51162", "#303F9F"};

    public SpecialityAdapter(Context context, List<String> specialityList) {
        mContext = context;
        this.specialityList = specialityList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<String>();
        this.arrayList.addAll(specialityList);
    }

    @Override
    public int getCount() {
        return specialityList.size();
    }

    @Override
    public Object getItem(int position) {
        return specialityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.speciality_row,null);

        TextView circleText = convertView.findViewById(R.id.circle_text);
        TextView speciality = convertView.findViewById(R.id.doctorFullName);

        String spec = specialityList.get(position);

        speciality.setText(spec);
        circleText.setText((spec.charAt(0)+"").toUpperCase());
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        specialityList.clear();
        if (charText.length() == 0) {
            specialityList.addAll(arrayList);
        } else {
            for (String s : arrayList) {
                if (s.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    specialityList.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

}
