package com.arraybit.modal;

public class Person {

    int index;
    String name;
    String mobile;
    int person;


    public Person(int index, String name, String mobile, int person) {
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
}
