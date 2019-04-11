package snaprank.example.labdadm.snaprank.models;

import java.util.ArrayList;

public class Usuario {
    private String username,localizacion;
    private String id;
    private String profilePicUrl;
    private int score;
    private ArrayList<Logro> awards;

    public Usuario(String id, String username, String localizacion, String profilePicUrl, int score, ArrayList<Logro> awards) {
        this.username = username;
        this.localizacion = localizacion;
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

    public String getLocalizacion() {
        return this.localizacion;
    }

    public String getProfilePicUrl() {
        return this.profilePicUrl;
    }

    public int getScore() { return this.score; }

    public ArrayList<Logro> getAwards() { return this.awards; }
}
