package com.arraybit.modal;

public class WaitingPerson {

    int index;
    String name;
    String mobile;
    int person;
    int status;

    public WaitingPerson(int index, String name, String mobile, int person,int status) {
        this.index = index;
        this.name = name;
        this.mobile = mobile;
        this.person = person;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
