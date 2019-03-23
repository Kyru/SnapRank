package snaprank.example.labdadm.snaprank;

public class Logro {

    private String category;
    private String fechaDesbloqueo;
    private int imageLogro;

    public Logro(String category, String fechaDesbloqueo, int imageLogro) {
        this.category = category;
        this.fechaDesbloqueo = fechaDesbloqueo;
        this.imageLogro = imageLogro;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFechaDesbloqueo() {
        return fechaDesbloqueo;
    }

    public void setFechaDesbloqueo(String fechaDesbloqueo) {
        this.fechaDesbloqueo = fechaDesbloqueo;
    }

    public int getImageLogro() {
        return imageLogro;
    }

    public void setImageLogro(int imageLogro) {
        this.imageLogro = imageLogro;
    }
}
