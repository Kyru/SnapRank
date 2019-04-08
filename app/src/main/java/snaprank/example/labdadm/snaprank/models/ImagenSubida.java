package snaprank.example.labdadm.snaprank.models;

public class ImagenSubida {

    private String username;
    private String description;
    private String category;
    private String location;
    private String url;
    private int likes;
    private int dislikes;
    private String id;

    public ImagenSubida(String username, String description, String category, String location, String url, int likes, int dislikes, String id) {
        this.username = username;
        this.description = description;
        this.category = category;
        this.location = location;
        this.url = url;
        this.likes = likes;
        this.dislikes = dislikes;
        this.id = id;
    }

    public ImagenSubida(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
