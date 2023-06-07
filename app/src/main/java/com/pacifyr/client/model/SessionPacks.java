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

        @SerializedName("purchased")
        @Expose
        private Boolean purchased;

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

        @SerializedName("numberOfRemainingSessions")
        @Expose
        private Integer numberOfRemainingSessions;

        @SerializedName("sessionPack")
        @Expose
        private SessionPack sessionPack;

        @SerializedName("provider")
        @Expose
        private User provider;

        public Boolean getPurchased() {
            return purchased;
        }

        public void setPurchased(Boolean purchased) {
            this.purchased = purchased;
        }

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

        public Integer getNumberOfRemainingSessions() {
            return numberOfRemainingSessions;
        }

        public void setNumberOfRemainingSessions(Integer numberOfRemainingSessions) {
            this.numberOfRemainingSessions = numberOfRemainingSessions;
        }

        public SessionPack getSessionPack() {
            return sessionPack;
        }

        public void setSessionPack(SessionPack sessionPack) {
            this.sessionPack = sessionPack;
        }

        public User getProvider() {
            return provider;
        }

        public void setProvider(User provider) {
            this.provider = provider;
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

    public class SessionPack {

        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("subscribed")
        @Expose
        private Boolean subscribed;
        @SerializedName("expired")
        @Expose
        private Boolean expired;
        @SerializedName("purchased")
        @Expose
        private Boolean purchased;
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

        public Boolean getSubscribed() {
            return subscribed;
        }

        public void setSubscribed(Boolean subscribed) {
            this.subscribed = subscribed;
        }

        public Boolean getExpired() {
            return expired;
        }

        public void setExpired(Boolean expired) {
            this.expired = expired;
        }

        public Boolean getPurchased() {
            return purchased;
        }

        public void setPurchased(Boolean purchased) {
            this.purchased = purchased;
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

    }

}