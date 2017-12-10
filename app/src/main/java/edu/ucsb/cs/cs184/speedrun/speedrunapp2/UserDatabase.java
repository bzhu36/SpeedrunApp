package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

/**
 * Created by icema on 12/10/2017.
 */

public class UserDatabase {
    private String email;
    private String name;

    public UserDatabase() {
    }

    public UserDatabase(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
