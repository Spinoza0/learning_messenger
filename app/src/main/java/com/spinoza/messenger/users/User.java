package com.spinoza.messenger.users;

public class User {
    private String id;
    private String name;
    private String lastname;
    private int age;
    private boolean online;

    public User() {
    }

    public User(String id, String name, String lastname, String ageText, boolean isOnline) {
        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (Exception e) {
            age = 0;
        }

        this.id = id;
        this.name = trimString(name);
        this.lastname = trimString(lastname);
        if (age < 0) {
            age = 0;
        }
        this.age = age;
        this.online = isOnline;
    }

    public User(String id, String name, String lastname, int age, boolean isOnline) {
        this.id = id;
        this.name = trimString(name);
        this.lastname = trimString(lastname);
        if (age < 0) {
            age = 0;
        }
        this.age = age;
        this.online = isOnline;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public boolean isOnline() {
        return online;
    }

    public static String trimString(String text) {
        String result = "";
        if (text != null)
            result = text.trim();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", isOnline=" + online +
                '}';
    }
}
