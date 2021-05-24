package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResourceMain implements Serializable
{

    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    private final static long serialVersionUID = 9165406563802137685L;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    public class Resource implements Serializable
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
        private final static long serialVersionUID = 2715543060235676186L;

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


    public class Result implements Serializable
    {

        @SerializedName("speciality")
        @Expose
        private String speciality;
        @SerializedName("lastItemAddedAt")
        @Expose
        private String lastItemAddedAt;
        @SerializedName("resources")
        @Expose
        private List<Resource> resources = null;
        private final static long serialVersionUID = 2240028562043688469L;

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getLastItemAddedAt() {
            return lastItemAddedAt;
        }

        public void setLastItemAddedAt(String lastItemAddedAt) {
            this.lastItemAddedAt = lastItemAddedAt;
        }

        public List<Resource> getResources() {
            return resources;
        }

        public void setResources(List<Resource> resources) {
            this.resources = resources;
        }

    }
}