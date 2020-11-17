package com.example.flutterpluginphoneinfo;


public class ContactsBean  {

    private String id;
    private String phone;
    private String name;
    private String email;
    private String contactTimes;
    private String times;

    public void setContactTimes(String contactTimes) {
        this.contactTimes = contactTimes;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public String getContactTimes() {
        return contactTimes == null ? "" : contactTimes;
    }

    public String getTimes() {
        return times == null ? "" : times;
    }




}
