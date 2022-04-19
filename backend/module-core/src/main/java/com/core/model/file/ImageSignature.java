package com.core.model.file;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageSignature {
    private int page;
    private float ph;
    private float pw;
    private float px;
    private float py;
    private String imageData;
    private String imagePath;

    public ImageSignature() {
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public float getPw() {
        return pw;
    }

    public void setPw(float pw) {
        this.pw = pw;
    }

    public float getPx() {
        return px;
    }

    public void setPx(float px) {
        this.px = px;
    }

    public float getPy() {
        return py;
    }

    public void setPy(float py) {
        this.py = py;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ImageSignature{" +
                "page=" + page +
                ", ph=" + ph +
                ", pw=" + pw +
                ", px=" + px +
                ", py=" + py +
                ", imageData='" + imageData + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
