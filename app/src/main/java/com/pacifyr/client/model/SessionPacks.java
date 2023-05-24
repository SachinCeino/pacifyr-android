package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SessionPacks implements Serializable {

    @SerializedName("results")
    @Expose
    private List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public class Result implements Serializable {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;

        @SerializedName("subscribed")
        @Expose
        private Boolean isSubscribed;
        @SerializedName("expired")
        @Expose
        private Boolean expired;
        @SerializedName("price")
        @Expose
        private Integer price;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("slotInterval")
        @Expose
        private Integer slotInterval;
        @SerializedName("numberOfSessions")
        @Expose
        private Integer numberOfSessions;
        @SerializedName("updatedAt")
        @Expose
        private Object updatedAt;

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Boolean getExpired() {
            return expired;
        }

        public void setExpired(Boolean expired) {
            this.expired = expired;
        }

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Integer getSlotInterval() {
            return slotInterval;
        }

        public void setSlotInterval(Integer slotInterval) {
            this.slotInterval = slotInterval;
        }

        public Integer getNumberOfSessions() {
            return numberOfSessions;
        }

        public void setNumberOfSessions(Integer numberOfSessions) {
            this.numberOfSessions = numberOfSessions;
        }

        public Object getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Object updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Boolean getSubscribed() {
            return isSubscribed;
        }

        public void setSubscribed(Boolean subscribed) {
            isSubscribed = subscribed;
        }
    }

}