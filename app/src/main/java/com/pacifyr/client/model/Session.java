package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Session {

    @SerializedName("readStatus")
    @Expose
    private Boolean readStatus;
    @SerializedName("sessionRatings")
    @Expose
    private List<Object> sessionRatings = null;
    @SerializedName("pacifyrEarnings")
    @Expose
    private Double pacifyrEarnings;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("pacifyrId")
    @Expose
    private String pacifyrId;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("afterSessionPacifyrEmotionId")
    @Expose
    private Object afterSessionPacifyrEmotionId;
    @SerializedName("sessionType")
    @Expose
    private String sessionType;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("sessionCost")
    @Expose
    private Double sessionCost;
    @SerializedName("stopped")
    @Expose
    private Boolean stopped;
    @SerializedName("afterSessionCustomerEmotionId")
    @Expose
    private Object afterSessionCustomerEmotionId;
    @SerializedName("missed")
    @Expose
    private Boolean missed;
    @SerializedName("errorMessage")
    @Expose
    private Object errorMessage;
    @SerializedName("billedSession")
    @Expose
    private Boolean billedSession;
    @SerializedName("pacifyr")
    @Expose
    private Pacifyr pacifyr;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("creditCardCharges")
    @Expose
    private Double creditCardCharges;
    @SerializedName("connected")
    @Expose
    private Boolean connected;
    @SerializedName("anonymousCall")
    @Expose
    private Boolean anonymousCall;
    @SerializedName("customerRating")
    @Expose
    private Integer customerRating;
    @SerializedName("declined")
    @Expose
    private Boolean declined;
    @SerializedName("billingStartTime")
    @Expose
    private String billingStartTime;
    @SerializedName("reasonForTheCall")
    @Expose
    private String reasonForTheCall;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("aggregationSessionId")
    @Expose
    private Object aggregationSessionId;
    @SerializedName("appSource")
    @Expose
    private String appSource;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("scheduledSessionId")
    @Expose
    private Object scheduledSessionId;
    private String timeSlotBookingId;
    private String twilioRoomSID;

    public String getTwilioRoomSID() {
        return twilioRoomSID;
    }

    public void setTwilioRoomSID(String twilioRoomSID) {
        this.twilioRoomSID = twilioRoomSID;
    }
    public String getTimeSlotBookingId() {
        return timeSlotBookingId;
    }

    public void setTimeSlotBookingId(String timeSlotBookingId) {
        this.timeSlotBookingId = timeSlotBookingId;
    }

    public Boolean getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Boolean readStatus) {
        this.readStatus = readStatus;
    }

    public List<Object> getSessionRatings() {
        return sessionRatings;
    }

    public void setSessionRatings(List<Object> sessionRatings) {
        this.sessionRatings = sessionRatings;
    }

    public Double getPacifyrEarnings() {
        return pacifyrEarnings;
    }

    public void setPacifyrEarnings(Double pacifyrEarnings) {
        this.pacifyrEarnings = pacifyrEarnings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPacifyrId() {
        return pacifyrId;
    }

    public void setPacifyrId(String pacifyrId) {
        this.pacifyrId = pacifyrId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getAfterSessionPacifyrEmotionId() {
        return afterSessionPacifyrEmotionId;
    }

    public void setAfterSessionPacifyrEmotionId(Object afterSessionPacifyrEmotionId) {
        this.afterSessionPacifyrEmotionId = afterSessionPacifyrEmotionId;
    }

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Double getSessionCost() {
        return sessionCost;
    }

    public void setSessionCost(Double sessionCost) {
        this.sessionCost = sessionCost;
    }

    public Boolean getStopped() {
        return stopped;
    }

    public void setStopped(Boolean stopped) {
        this.stopped = stopped;
    }

    public Object getAfterSessionCustomerEmotionId() {
        return afterSessionCustomerEmotionId;
    }

    public void setAfterSessionCustomerEmotionId(Object afterSessionCustomerEmotionId) {
        this.afterSessionCustomerEmotionId = afterSessionCustomerEmotionId;
    }

    public Boolean getMissed() {
        return missed;
    }

    public void setMissed(Boolean missed) {
        this.missed = missed;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Object errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Boolean getBilledSession() {
        return billedSession;
    }

    public void setBilledSession(Boolean billedSession) {
        this.billedSession = billedSession;
    }

    public Pacifyr getPacifyr() {
        return pacifyr;
    }

    public void setPacifyr(Pacifyr pacifyr) {
        this.pacifyr = pacifyr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getCreditCardCharges() {
        return creditCardCharges;
    }

    public void setCreditCardCharges(Double creditCardCharges) {
        this.creditCardCharges = creditCardCharges;
    }

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public Boolean getAnonymousCall() {
        return anonymousCall;
    }

    public void setAnonymousCall(Boolean anonymousCall) {
        this.anonymousCall = anonymousCall;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public Boolean getDeclined() {
        return declined;
    }

    public void setDeclined(Boolean declined) {
        this.declined = declined;
    }

    public String getBillingStartTime() {
        return billingStartTime;
    }

    public void setBillingStartTime(String billingStartTime) {
        this.billingStartTime = billingStartTime;
    }

    public String getReasonForTheCall() {
        return reasonForTheCall;
    }

    public void setReasonForTheCall(String reasonForTheCall) {
        this.reasonForTheCall = reasonForTheCall;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Object getAggregationSessionId() {
        return aggregationSessionId;
    }

    public void setAggregationSessionId(Object aggregationSessionId) {
        this.aggregationSessionId = aggregationSessionId;
    }

    public String getAppSource() {
        return appSource;
    }

    public void setAppSource(String appSource) {
        this.appSource = appSource;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Object getScheduledSessionId() {
        return scheduledSessionId;
    }

    public void setScheduledSessionId(Object scheduledSessionId) {
        this.scheduledSessionId = scheduledSessionId;
    }

}