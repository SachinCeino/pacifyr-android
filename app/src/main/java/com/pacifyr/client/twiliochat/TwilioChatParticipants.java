package com.pacifyr.client.twiliochat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pacifyrapp.utilitylibrary.model.pacifyr.MUser;

import java.io.Serializable;
import java.util.List;

public class TwilioChatParticipants {

    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("results")
    @Expose
    private List<TwilioConversationCreate> results = null;
    private final static long serialVersionUID = 493448354181316765L;

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<TwilioConversationCreate> getResults() {
        return results;
    }

    public void setResults(List<TwilioConversationCreate> results) {
        this.results = results;
    }


    public class Result implements Serializable
    {

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
        private List<TwilioConversationCreate.Unreadstatus> unreadStatus = null;
        @SerializedName("user")
        @Expose
        private MUser user;
        @SerializedName("participants")
        @Expose
        private List<String> participants = null;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        private final static long serialVersionUID = -8697856684556210757L;

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

        public List<TwilioConversationCreate.Unreadstatus> getUnreadStatus() {
            return unreadStatus;
        }

        public void setUnreadStatus(List<TwilioConversationCreate.Unreadstatus> unreadStatus) {
            this.unreadStatus = unreadStatus;
        }

        public MUser getUser() {
            return user;
        }

        public void setUser(MUser user) {
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

    }


    class CouponCode implements Serializable
    {

        @SerializedName("discountPercentage")
        @Expose
        private Integer discountPercentage;
        @SerializedName("companyId")
        @Expose
        private String companyId;
        @SerializedName("code")
        @Expose
        private String code;
        @SerializedName("validityInDays")
        @Expose
        private Integer validityInDays;
        @SerializedName("freeMinutes")
        @Expose
        private Integer freeMinutes;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("type")
        @Expose
        private Object type;
        private final static long serialVersionUID = -429140096618726697L;

        public Integer getDiscountPercentage() {
            return discountPercentage;
        }

        public void setDiscountPercentage(Integer discountPercentage) {
            this.discountPercentage = discountPercentage;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getValidityInDays() {
            return validityInDays;
        }

        public void setValidityInDays(Integer validityInDays) {
            this.validityInDays = validityInDays;
        }

        public Integer getFreeMinutes() {
            return freeMinutes;
        }

        public void setFreeMinutes(Integer freeMinutes) {
            this.freeMinutes = freeMinutes;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getType() {
            return type;
        }

        public void setType(Object type) {
            this.type = type;
        }

    }
}
