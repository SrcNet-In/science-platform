package org.opencadc.skaha.image;

import java.util.List;

public class ImageList {
    private List<Image> images;

    public ImageList() {
    }

    public ImageList(List<Image> images) {
        this.images = images;
    }

    public void set(List<Image> images) {
        this.images = images;
    }

    public List<Image> get() {
        return images;
    }
}
