package snaprank.example.labdadm.snaprank;

public class ImagenRanking {
    private String id;
    private int imageId;

    public ImagenRanking(String id, int imageId) {
        this.id = id;
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
