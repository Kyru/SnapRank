package snaprank.example.labdadm.snaprank.adapters;

public class CategoryData {
    private String nombre;
    private Integer id;
    private Integer image;

    public CategoryData(Integer id, String nombre,Integer image) {
        this.nombre = nombre;
        this.id = id;
        this.image=image;
    }

    public Integer getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Integer getImage() {
        return this.image;
    }
}
