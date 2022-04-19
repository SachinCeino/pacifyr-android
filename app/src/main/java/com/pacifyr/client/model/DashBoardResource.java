package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyr.client.R;

import java.io.Serializable;
import java.util.List;

/**

 */

public class DashBoardResource implements Serializable{

        @SerializedName("total_count")
        @Expose
        private Integer totalCount;
        @SerializedName("results")
        @Expose
        private List<Result> results = null;

        public Integer getTotalCount() {
        return totalCount;
    }

        public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

        public List<Result> getResults() {
        return results;
    }

        public void setResults(List<Result> results) {
        this.results = results;
    }

    public class Result implements Serializable
    {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("speciality")
        @Expose
        private List<String> speciality = null;
        @SerializedName("author")
        @Expose
        private String author;
        @SerializedName("document")
        @Expose
        private Object document;
        @SerializedName("description")
        @Expose
        private Object description;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("video")
        @Expose
        private Object video;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("url")
        @Expose
        private String url;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        private final static long serialVersionUID = 7198728902587568835L;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public List<String> getSpeciality() {
            return speciality;
        }

        public void setSpeciality(List<String> speciality) {
            this.speciality = speciality;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Object getDocument() {
            return document;
        }

        public void setDocument(Object document) {
            this.document = document;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getVideo() {
            return video;
        }

        public void setVideo(Object video) {
            this.video = video;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}



