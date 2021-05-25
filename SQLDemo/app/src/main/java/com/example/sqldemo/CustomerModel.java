package com.example.sqldemo;

public class CustomerModel {
    private String name;
    private int id;
    private int age;
    private boolean isActive;

    public CustomerModel( int id,String name, int age, boolean isActive) {
        this.name = name;
        this.id = id;
        this.age = age;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "CustomerValue{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", age=" + age +
                ", isActive=" + isActive +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
