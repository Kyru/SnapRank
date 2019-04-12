package snaprank.example.labdadm.snaprank.models;

import java.util.ArrayList;

public class Usuario {
    private String username,location;
    private String id;
    private String profilePicUrl;
    private int score;
    private ArrayList<Logro> awards;

    public Usuario(String id, String username, String location, String profilePicUrl, int score, ArrayList<Logro> awards) {
        this.username = username;
        this.location = location;
        this.id = id;
        this.profilePicUrl = profilePicUrl;
        this.score = score;
        this.awards = awards;
    }
    public Usuario() {  }

    public String getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfilePicUrl() {
        return this.profilePicUrl;
    }

    public int getScore() { return this.score; }
    public void setScore(int s) { this.score=s; }

    public ArrayList<Logro> getAwards() { return this.awards; }
}
