package com.example.healthcare.models;

public class Doctor implements Comparable<Doctor> {
    private String fullName;
    private String code;
    private String phoneNumber;
    private String email;
    private String city;
    private String address;
    private String speciality;

    public Doctor() {
    }

    public Doctor(String fullName, String code, String phoneNumber, String email, String city, String address, String speciality) {
        this.fullName = fullName;
        this.code = code;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.city = city;
        this.address = address;
        this.speciality = speciality;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public int compareTo(Doctor o) {
        return this.getFullName().compareTo(o.getFullName());
    }
}
