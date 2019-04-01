package com.example.demogallerycontentprovider;

public class ImageModel {
    public String path;
    public String name;

    public ImageModel(String path, String name) {
        this.path = path;
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
