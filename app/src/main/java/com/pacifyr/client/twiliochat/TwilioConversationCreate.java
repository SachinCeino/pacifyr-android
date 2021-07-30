package com.pacifyr.client.twiliochat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

import java.io.Serializable;
import java.util.List;

public class TwilioConversationCreate implements Serializable {
    @SerializedName("totalMessageCount")
    @Expose
    private Integer totalMessageCount;
    @SerializedName("lastMessage")
    @Expose
    private String lastMessage;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("pacifyr")
    @Expose
    private MUser pacifyr;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("pacifyrId")
    @Expose
    private String pacifyrId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("lastMessageAt")
    @Expose
    private String lastMessageAt;
    @SerializedName("twilioConversationId")
    @Expose
    private String twilioConversationId;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("unreadStatus")
    @Expose
    private List<Unreadstatus> unreadStatus = null;
    @SerializedName("user")
    @Expose
    private Object user;
    @SerializedName("participants")
    @Expose
    private List<String> participants = null;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    private final static long serialVersionUID = 6291081706746180834L;

    public Integer getTotalMessageCount() {
        return totalMessageCount;
    }

    public void setTotalMessageCount(Integer totalMessageCount) {
        this.totalMessageCount = totalMessageCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public MUser getPacifyr() {
        return pacifyr;
    }

    public void setPacifyr(MUser pacifyr) {
        this.pacifyr = pacifyr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPacifyrId() {
        return pacifyrId;
    }

    public void setPacifyrId(String pacifyrId) {
        this.pacifyrId = pacifyrId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(String lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }

    public String getTwilioConversationId() {
        return twilioConversationId;
    }

    public void setTwilioConversationId(String twilioConversationId) {
        this.twilioConversationId = twilioConversationId;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Unreadstatus> getUnreadStatus() {
        return unreadStatus;
    }

    public void setUnreadStatus(List<Unreadstatus> unreadStatus) {
        this.unreadStatus = unreadStatus;
    }

    public Object getUser() {
        return user;
    }

    public void setUser(Object user) {
        this.user = user;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    class Unreadstatus implements Serializable
    {

        @SerializedName("unreadCount")
        @Expose
        private Integer unreadCount;
        @SerializedName("userId")
        @Expose
        private String userId;
        private final static long serialVersionUID = 4427920644066208315L;

        public Integer getUnreadCount() {
            return unreadCount;
        }

        public void setUnreadCount(Integer unreadCount) {
            this.unreadCount = unreadCount;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

    }

}

