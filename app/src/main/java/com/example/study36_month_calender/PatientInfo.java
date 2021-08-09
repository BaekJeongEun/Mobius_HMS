package com.example.study36_month_calender;

public class PatientInfo {

    String name;
    String gender;
    String age;
    String disease;
    String time;
    String phone;

    public PatientInfo(String name, String gender, String age, String disease, String time, String phone){
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.disease = disease;
        this.time = time;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
