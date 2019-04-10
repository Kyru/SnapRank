package snaprank.example.labdadm.snaprank.models;

public class Usuario {
    private String username,localizacion;
    private String id;
    private String profilePicUrl;

    public Usuario(String id, String username, String localizacion, String profilePicUrl) {
        this.username = username;
        this.localizacion = localizacion;
        this.id = id;
        this.profilePicUrl = profilePicUrl;
    }

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
}
