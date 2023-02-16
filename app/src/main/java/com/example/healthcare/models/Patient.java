package com.example.healthcare.models;

public class Patient implements Comparable<Patient> {
    private String firstName;
    private String lastName;
    private String birthDate;
    private String phoneNumber;
    private String email;
    private String cin;
    private String maritalStatus;

    public Patient() {
    }

    public Patient(String firstName, String lastName, String birthDate, String phoneNumber, String email, String cin, String maritalStatus) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.cin = cin;
        this.maritalStatus = maritalStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getFullName()
    {
        return firstName+" "+lastName;
    }

    @Override
    public int compareTo(Patient o) {
        return this.getFullName().compareTo(o.getFullName());
    }
}
