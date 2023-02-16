package com.example.healthcare.models;

public class Appointment {
    String date;
    String time;
    String emailDoctor;
    String emailPatient;
    String status;
    static String[] statusValues = {"On hold", "Accepted", "Declined"};

    public Appointment() {

    }

    public Appointment(String date, String time, String emailDoctor, String emailPatient) {
        this.date = date;
        this.time = time;
        this.emailDoctor = emailDoctor;
        this.emailPatient = emailPatient;
        this.status = statusValues[0];
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEmailDoctor() {
        return emailDoctor;
    }

    public void setEmailDoctor(String emailDoctor) {
        this.emailDoctor = emailDoctor;
    }

    public String getEmailPatient() {
        return emailPatient;
    }

    public void setEmailPatient(String emailPatient) {
        this.emailPatient = emailPatient;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusAccepted() {
        this.status = statusValues[1];
    }

    public void setStatusDeclined() {
        this.status = statusValues[2];
    }
}
