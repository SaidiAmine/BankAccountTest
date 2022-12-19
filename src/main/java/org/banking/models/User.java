package org.banking.models;

public class User {

    private String password;
    private String rib;
    private String fullName;

    public User(String password, String rib, String fullName) {
        this.password = password;
        this.rib = rib;
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getRib() {
        return rib;
    }

    public String getFullName() {
        return fullName;
    }
}
