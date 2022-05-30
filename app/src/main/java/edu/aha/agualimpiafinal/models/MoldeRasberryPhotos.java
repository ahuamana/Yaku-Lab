package edu.aha.agualimpiafinal.models;

import java.util.List;

public class MoldeRasberryPhotos {

    List<String> image;

    public MoldeRasberryPhotos() {
    }

    public MoldeRasberryPhotos(List<String> image) {
        this.image = image;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }
}
