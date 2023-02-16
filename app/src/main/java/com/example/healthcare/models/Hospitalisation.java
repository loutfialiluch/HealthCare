package com.example.healthcare.models;

public class Hospitalisation implements Comparable<Hospitalisation> {
    String hospitalName;
    String emailPatient;
    String date;
    String disease;
    String price;

    public Hospitalisation() {
    }

    public Hospitalisation(String hospitalName, String emailPatient, String date, String disease, String price) {
        this.hospitalName = hospitalName;
        this.emailPatient = emailPatient;
        this.date = date;
        this.disease = disease;
        this.price = price;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getEmailPatient() {
        return emailPatient;
    }

    public void setEmailPatient(String emailPatient) {
        this.emailPatient = emailPatient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int compareTo(Hospitalisation o) {
        String date2 = o.getDate();
        String[] dateSplitted1 = date.split("/");
        String[] dateSplitted2 = date2.split("/");
        if(dateSplitted1[0] != dateSplitted2[0])
        {
            return Integer.parseInt(dateSplitted1[0]) - Integer.parseInt(dateSplitted1[0]);
        }
        else if(dateSplitted1[1] != dateSplitted2[1])
        {
            return Integer.parseInt(dateSplitted1[1]) - Integer.parseInt(dateSplitted1[1]);
        }
        else if(dateSplitted1[2] != dateSplitted2[2])
        {
            return Integer.parseInt(dateSplitted1[2]) - Integer.parseInt(dateSplitted1[2]);
        }
        else
            return 0;
    }
}
