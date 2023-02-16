package com.example.healthcare.models;

public class Relationship {
    String emailDoctor;
    String emailPatient;

    public Relationship() {
    }

    public Relationship(String emailDoctor, String emailPatient) {
        this.emailDoctor = emailDoctor;
        this.emailPatient = emailPatient;
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
}
