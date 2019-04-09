package snaprank.example.labdadm.snaprank.models;

public class Usuario {
    private String nombre,localizacion;
    private Integer id;
    private Integer image;
    private String profilePicUrl;

    public Usuario(Integer id, String nombre, String localizacion, Integer image) {
        this.nombre = nombre;
        this.localizacion = localizacion;
        this.id = id;
        this.image=image;
        this.profilePicUrl = profilePicUrl;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }
    public String getLocalizacion() {
        return this.localizacion;
    }
    public Integer getImage() {
        return this.image;
    }
    public String getProfilePicUrl() { return this.profilePicUrl; }
}
