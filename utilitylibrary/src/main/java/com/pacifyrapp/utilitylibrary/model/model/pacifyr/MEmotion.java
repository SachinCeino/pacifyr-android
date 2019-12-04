package com.pacifyrapp.utilitylibrary.model.model.pacifyr;

/**
 * Created by Mani-Ceino on 12/21/2017.
 */

public class MEmotion {


    private String id;
    private  boolean isClicked=false;
    private String createdAt ;
    private String updatedAt ;
    private String title;
    private String image;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public boolean getClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
    @Override
    public String toString() {
        return "";
       /* return "SpecialityModel [id=" + id + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt + ", title=" + title + ", image="
                + image + "]";*/
    }
}
