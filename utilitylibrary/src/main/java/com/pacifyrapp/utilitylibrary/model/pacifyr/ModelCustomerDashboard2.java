package com.pacifyrapp.utilitylibrary.model.pacifyr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by developer on 24/July/2017.
 */

public class ModelCustomerDashboard2
{
    private ThisMonthStatistics thisMonthStatistics;

    private MLastSession lastSession;

    private Integer videoVisits;
    private Integer officeVisits;

    public Integer getVideoVisits() {
        return videoVisits;
    }

    public void setVideoVisits(Integer videoVisits) {
        this.videoVisits = videoVisits;
    }

    public Integer getOfficeVisits() {
        return officeVisits;
    }

    public void setOfficeVisits(Integer officeVisits) {
        this.officeVisits = officeVisits;
    }

    private User user;

    private ThisWeekStatistics thisWeekStatistics;

    private MUser[] preferredPacifyrs;
    private NextAppointment nextAppointment;
    private Integer totalUnreadMessages;
    private LatestUnreadConversation latestUnreadConversation;
    public NextAppointment getNextAppointment() {
        return nextAppointment;
    }

    public Integer getTotalUnreadMessages() {
        return totalUnreadMessages;
    }

    public void setTotalUnreadMessages(Integer totalUnreadMessages) {
        this.totalUnreadMessages = totalUnreadMessages;
    }

    public LatestUnreadConversation getLatestUnreadConversation() {
        return latestUnreadConversation;
    }

    public void setLatestUnreadConversation(LatestUnreadConversation latestUnreadConversation) {
        this.latestUnreadConversation = latestUnreadConversation;
    }

    public void setNextAppointment(NextAppointment nextAppointment) {
        this.nextAppointment = nextAppointment;
    }
    public MUser[] getPreferredPacifyrs ()
    {
        return preferredPacifyrs;
    }

    public void setPreferredPacifyrs (MUser[] preferredPacifyrs)
    {
        this.preferredPacifyrs = preferredPacifyrs;
    }



    public ThisMonthStatistics getThisMonthStatistics ()
    {
        return thisMonthStatistics;
    }

    public void setThisMonthStatistics (ThisMonthStatistics thisMonthStatistics)
    {
        this.thisMonthStatistics = thisMonthStatistics;
    }

    public MLastSession getLastSession ()
    {
        return lastSession;
    }

    public void setLastSession (MLastSession lastSession)
    {
        this.lastSession = lastSession;
    }



    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    public ThisWeekStatistics getThisWeekStatistics ()
    {
        return thisWeekStatistics;
    }

    public void setThisWeekStatistics (ThisWeekStatistics thisWeekStatistics)
    {
        this.thisWeekStatistics = thisWeekStatistics;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [thisMonthStatistics = "+thisMonthStatistics+", lastSession = "+lastSession+", preferredPacifyrs = "+preferredPacifyrs+", user = "+user+", thisWeekStatistics = "+thisWeekStatistics+", videoVisits = "+videoVisits+", officeVisits = "+officeVisits+"]";
    }


    public class Unreadstatus implements Serializable
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


    public class LatestUnreadConversation implements Serializable
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
        private Pacifyr pacifyr;
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
        private final static long serialVersionUID = 1159751122286272283L;

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

    }

  /*  public class LastSession
    {
        public SessionRatings[] sessionRatings;

        private String sessionCost;

        private String creditCardCharges;

        private String connected;

        private String sessionType;

        private String endTime;

        private String updatedAt;

        private String id;

        private String startTime;

        private String pacifyrId;

        private String afterSessionPacifyrEmotionId;

        private String duration;

        private String pacifyrEarnings;

        private String stopped;

        private String createdAt;

        private Pacifyr pacifyr;

        private String userId;

        private String afterSessionCustomerEmotionId;

        private User user;

        private String aggregationSessionId;

        private String customerRating;

        public SessionRatings[] getSessionRatings ()
        {
            return sessionRatings;
        }

        public void setSessionRatings (SessionRatings[] sessionRatings)
        {
            this.sessionRatings = sessionRatings;
        }



        public String getSessionCost ()
        {
            return sessionCost;
        }

        public void setSessionCost (String sessionCost)
        {
            this.sessionCost = sessionCost;
        }

        public String getCreditCardCharges ()
        {
            return creditCardCharges;
        }

        public void setCreditCardCharges (String creditCardCharges)
        {
            this.creditCardCharges = creditCardCharges;
        }

        public String getConnected ()
        {
            return connected;
        }

        public void setConnected (String connected)
        {
            this.connected = connected;
        }

        public String getSessionType ()
        {
            return sessionType;
        }

        public void setSessionType (String sessionType)
        {
            this.sessionType = sessionType;
        }

        public String getEndTime ()
        {
            return endTime;
        }

        public void setEndTime (String endTime)
        {
            this.endTime = endTime;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getStartTime ()
        {
            return startTime;
        }

        public void setStartTime (String startTime)
        {
            this.startTime = startTime;
        }

        public String getPacifyrId ()
        {
            return pacifyrId;
        }

        public void setPacifyrId (String pacifyrId)
        {
            this.pacifyrId = pacifyrId;
        }

        public String getAfterSessionPacifyrEmotionId ()
        {
            return afterSessionPacifyrEmotionId;
        }

        public void setAfterSessionPacifyrEmotionId (String afterSessionPacifyrEmotionId)
        {
            this.afterSessionPacifyrEmotionId = afterSessionPacifyrEmotionId;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getPacifyrEarnings ()
        {
            return pacifyrEarnings;
        }

        public void setPacifyrEarnings (String pacifyrEarnings)
        {
            this.pacifyrEarnings = pacifyrEarnings;
        }

        public String getStopped ()
        {
            return stopped;
        }

        public void setStopped (String stopped)
        {
            this.stopped = stopped;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public Pacifyr getPacifyr ()
        {
            return pacifyr;
        }

        public void setPacifyr (Pacifyr pacifyr)
        {
            this.pacifyr = pacifyr;
        }

        public String getUserId ()
        {
            return userId;
        }

        public void setUserId (String userId)
        {
            this.userId = userId;
        }

        public String getAfterSessionCustomerEmotionId ()
        {
            return afterSessionCustomerEmotionId;
        }

        public void setAfterSessionCustomerEmotionId (String afterSessionCustomerEmotionId)
        {
            this.afterSessionCustomerEmotionId = afterSessionCustomerEmotionId;
        }

        public User getUser ()
        {
            return user;
        }

        public void setUser (User user)
        {
            this.user = user;
        }

        public String getAggregationSessionId ()
        {
            return aggregationSessionId;
        }

        public void setAggregationSessionId (String aggregationSessionId)
        {
            this.aggregationSessionId = aggregationSessionId;
        }

        public String getCustomerRating ()
        {
            return customerRating;
        }

        public void setCustomerRating (String customerRating)
        {
            this.customerRating = customerRating;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [sessionRatings = "+sessionRatings+", sessionCost = "+sessionCost+", creditCardCharges = "+creditCardCharges+", connected = "+connected+", sessionType = "+sessionType+", endTime = "+endTime+", updatedAt = "+updatedAt+", id = "+id+", startTime = "+startTime+", pacifyrId = "+pacifyrId+", afterSessionPacifyrEmotionId = "+afterSessionPacifyrEmotionId+", duration = "+duration+", pacifyrEarnings = "+pacifyrEarnings+", stopped = "+stopped+", createdAt = "+createdAt+", pacifyr = "+pacifyr+", userId = "+userId+", afterSessionCustomerEmotionId = "+afterSessionCustomerEmotionId+", user = "+user+", aggregationSessionId = "+aggregationSessionId+", customerRating = "+customerRating+"]";
        }
    }*/


    /*public class SessionRatings
    {
        private String updatedAt;

        private String id;

        private String effectiveness;

        private String sessionId;

        private String createdAt;

        private String userId;

        private String rating;

        private String beforeSessionEmotionId;

        private String type;

        private String comment;

        private String notes;

        private String afterSessionEmotionId;

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getEffectiveness ()
        {
            return effectiveness;
        }

        public void setEffectiveness (String effectiveness)
        {
            this.effectiveness = effectiveness;
        }

        public String getSessionId ()
        {
            return sessionId;
        }

        public void setSessionId (String sessionId)
        {
            this.sessionId = sessionId;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getUserId ()
        {
            return userId;
        }

        public void setUserId (String userId)
        {
            this.userId = userId;
        }

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        public String getBeforeSessionEmotionId ()
        {
            return beforeSessionEmotionId;
        }

        public void setBeforeSessionEmotionId (String beforeSessionEmotionId)
        {
            this.beforeSessionEmotionId = beforeSessionEmotionId;
        }

        public String getType ()
        {
            return type;
        }

        public void setType (String type)
        {
            this.type = type;
        }

        public String getComment ()
        {
            return comment;
        }

        public void setComment (String comment)
        {
            this.comment = comment;
        }

        public String getNotes ()
        {
            return notes;
        }

        public void setNotes (String notes)
        {
            this.notes = notes;
        }

        public String getAfterSessionEmotionId ()
        {
            return afterSessionEmotionId;
        }

        public void setAfterSessionEmotionId (String afterSessionEmotionId)
        {
            this.afterSessionEmotionId = afterSessionEmotionId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", effectiveness = "+effectiveness+", sessionId = "+sessionId+", createdAt = "+createdAt+", userId = "+userId+", rating = "+rating+", beforeSessionEmotionId = "+beforeSessionEmotionId+", type = "+type+", comment = "+comment+", notes = "+notes+", afterSessionEmotionId = "+afterSessionEmotionId+"]";
        }
    }*/


    public class NextAppointment implements Serializable
    {

        @SerializedName("rejected")
        @Expose
        private Boolean rejected;
        @SerializedName("visitNoteInternal")
        @Expose
        private Object visitNoteInternal;
        @SerializedName("pacifyrId")
        @Expose
        private String pacifyrId;
        @SerializedName("confirmed")
        @Expose
        private Boolean confirmed;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("rejectReason")
        @Expose
        private Object rejectReason;
        @SerializedName("startTimeGMT")
        @Expose
        private String startTimeGMT;
        @SerializedName("endTimeGMT")
        @Expose
        private String endTimeGMT;
        @SerializedName("visitNote")
        @Expose
        private Object visitNote;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("cancelReason")
        @Expose
        private Object cancelReason;
        @SerializedName("scheduleId")
        @Expose
        private String scheduleId;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("completedAt")
        @Expose
        private Object completedAt;
        @SerializedName("bookingRequestPaymentId")
        @Expose
        private String bookingRequestPaymentId;
        @SerializedName("completed")
        @Expose
        private Boolean completed;
        @SerializedName("pacifyr")
        @Expose
        private Pacifyr pacifyr;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("transactionId")
        @Expose
        private String transactionId;
        @SerializedName("timeSlotId")
        @Expose
        private String timeSlotId;
        @SerializedName("statusText")
        @Expose
        private String statusText;
        @SerializedName("preVisitNote")
        @Expose
        private String preVisitNote;
        @SerializedName("cancelled")
        @Expose
        private Boolean cancelled;
        @SerializedName("user")
        @Expose
        private Object user;
        @SerializedName("status")
        @Expose
        private String status;
        private final static long serialVersionUID = -6656502991815432813L;

        public Boolean getRejected() {
            return rejected;
        }

        public void setRejected(Boolean rejected) {
            this.rejected = rejected;
        }

        public Object getVisitNoteInternal() {
            return visitNoteInternal;
        }

        public void setVisitNoteInternal(Object visitNoteInternal) {
            this.visitNoteInternal = visitNoteInternal;
        }

        public String getPacifyrId() {
            return pacifyrId;
        }

        public void setPacifyrId(String pacifyrId) {
            this.pacifyrId = pacifyrId;
        }

        public Boolean getConfirmed() {
            return confirmed;
        }

        public void setConfirmed(Boolean confirmed) {
            this.confirmed = confirmed;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(Object rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getStartTimeGMT() {
            return startTimeGMT;
        }

        public void setStartTimeGMT(String startTimeGMT) {
            this.startTimeGMT = startTimeGMT;
        }

        public String getEndTimeGMT() {
            return endTimeGMT;
        }

        public void setEndTimeGMT(String endTimeGMT) {
            this.endTimeGMT = endTimeGMT;
        }

        public Object getVisitNote() {
            return visitNote;
        }

        public void setVisitNote(Object visitNote) {
            this.visitNote = visitNote;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getCancelReason() {
            return cancelReason;
        }

        public void setCancelReason(Object cancelReason) {
            this.cancelReason = cancelReason;
        }

        public String getScheduleId() {
            return scheduleId;
        }

        public void setScheduleId(String scheduleId) {
            this.scheduleId = scheduleId;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getCompletedAt() {
            return completedAt;
        }

        public void setCompletedAt(Object completedAt) {
            this.completedAt = completedAt;
        }

        public String getBookingRequestPaymentId() {
            return bookingRequestPaymentId;
        }

        public void setBookingRequestPaymentId(String bookingRequestPaymentId) {
            this.bookingRequestPaymentId = bookingRequestPaymentId;
        }

        public Boolean getCompleted() {
            return completed;
        }

        public void setCompleted(Boolean completed) {
            this.completed = completed;
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

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getTimeSlotId() {
            return timeSlotId;
        }

        public void setTimeSlotId(String timeSlotId) {
            this.timeSlotId = timeSlotId;
        }

        public String getStatusText() {
            return statusText;
        }

        public void setStatusText(String statusText) {
            this.statusText = statusText;
        }

        public String getPreVisitNote() {
            return preVisitNote;
        }

        public void setPreVisitNote(String preVisitNote) {
            this.preVisitNote = preVisitNote;
        }

        public Boolean getCancelled() {
            return cancelled;
        }

        public void setCancelled(Boolean cancelled) {
            this.cancelled = cancelled;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public class Pacifyr
    {
        private String qualifications;

        private String qbid;

        private String[] education;

        private String loginType;

        private String online;

        private String id;

        private String availableMinutes;

        private String phoneNumber;

        private Double walletBalance;

        private String[] specialities;

        private String createdAt;

        private String description;

        private String gender;

        private String firstName;

        private String bonusProfileId;

        private String roleId;

        private String lastName;

        private String emotionId;

        private String websiteURL;

        private String image;

        private String currentlyAvailable;

        private RoleModel roleModel;

        private Emotion emotion;

        private String updatedAt;

        private String[] certification;

        private String price;

        private String pin;

        private String numberOfReviews;

        private String email;

        private String companyId;

        private String rating;

        public String getQualifications ()
        {
            return qualifications;
        }

        public void setQualifications (String qualifications)
        {
            this.qualifications = qualifications;
        }

        public String getQbid ()
        {
            return qbid;
        }

        public void setQbid (String qbid)
        {
            this.qbid = qbid;
        }

        public String[] getEducation ()
        {
            return education;
        }

        public void setEducation (String[] education)
        {
            this.education = education;
        }

        public String getLoginType ()
        {
            return loginType;
        }

        public void setLoginType (String loginType)
        {
            this.loginType = loginType;
        }

        public String getOnline ()
        {
            return online;
        }

        public void setOnline (String online)
        {
            this.online = online;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getAvailableMinutes ()
        {
            return availableMinutes;
        }

        public void setAvailableMinutes (String availableMinutes)
        {
            this.availableMinutes = availableMinutes;
        }

        public String getPhoneNumber ()
        {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
        }

        public Double getWalletBalance ()
        {
            return walletBalance;
        }

        public void setWalletBalance (Double walletBalance)
        {
            this.walletBalance = walletBalance;
        }

        public String[] getSpecialities ()
        {
            return specialities;
        }

        public void setSpecialities (String[] specialities)
        {
            this.specialities = specialities;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getGender ()
        {
            return gender;
        }

        public void setGender (String gender)
        {
            this.gender = gender;
        }

        public String getFirstName ()
        {
            return firstName;
        }

        public void setFirstName (String firstName)
        {
            this.firstName = firstName;
        }

        public String getBonusProfileId ()
        {
            return bonusProfileId;
        }

        public void setBonusProfileId (String bonusProfileId)
        {
            this.bonusProfileId = bonusProfileId;
        }

        public String getRoleId ()
        {
            return roleId;
        }

        public void setRoleId (String roleId)
        {
            this.roleId = roleId;
        }

        public String getLastName ()
        {
            return lastName;
        }

        public void setLastName (String lastName)
        {
            this.lastName = lastName;
        }

        public String getEmotionId ()
        {
            return emotionId;
        }

        public void setEmotionId (String emotionId)
        {
            this.emotionId = emotionId;
        }

        public String getWebsiteURL ()
        {
            return websiteURL;
        }

        public void setWebsiteURL (String websiteURL)
        {
            this.websiteURL = websiteURL;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getCurrentlyAvailable ()
        {
            return currentlyAvailable;
        }

        public void setCurrentlyAvailable (String currentlyAvailable)
        {
            this.currentlyAvailable = currentlyAvailable;
        }

        public RoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (RoleModel roleModel)
        {
            this.roleModel = roleModel;
        }

        public Emotion getEmotion ()
        {
            return emotion;
        }

        public void setEmotion (Emotion emotion)
        {
            this.emotion = emotion;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String[] getCertification ()
        {
            return certification;
        }

        public void setCertification (String[] certification)
        {
            this.certification = certification;
        }

        public String getPrice ()
        {
            return price;
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getPin ()
        {
            return pin;
        }

        public void setPin (String pin)
        {
            this.pin = pin;
        }

        public String getNumberOfReviews ()
        {
            return numberOfReviews;
        }

        public void setNumberOfReviews (String numberOfReviews)
        {
            this.numberOfReviews = numberOfReviews;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", walletBalance = "+walletBalance+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", bonusProfileId = "+bonusProfileId+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", currentlyAvailable = "+currentlyAvailable+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", companyId = "+companyId+", rating = "+rating+"]";
        }
    }




/*

    public class PreferredPacifyrs
    {
        private String qualifications;

        private String qbid;

        private String[] education;

        private String loginType;

        private String online;

        private String id;

        private String availableMinutes;

        private String phoneNumber;

        private String walletBalance;

        private String[] specialities;

        private String createdAt;

        private String description;

        private String gender;

        private String firstName;

        private String bonusProfileId;

        private String roleId;

        private String lastName;

        private String emotionId;

        private String websiteURL;

        private String image;

        private String currentlyAvailable;

        private RoleModel roleModel;

        private Emotion emotion;

        private String updatedAt;

        private String[] certification;

        private String price;

        private String pin;

        private String numberOfReviews;

        private String email;

        private String companyId;

        private String rating;

        public String getQualifications ()
        {
            return qualifications;
        }

        public void setQualifications (String qualifications)
        {
            this.qualifications = qualifications;
        }

        public String getQbid ()
        {
            return qbid;
        }

        public void setQbid (String qbid)
        {
            this.qbid = qbid;
        }

        public String[] getEducation ()
        {
            return education;
        }

        public void setEducation (String[] education)
        {
            this.education = education;
        }

        public String getLoginType ()
        {
            return loginType;
        }

        public void setLoginType (String loginType)
        {
            this.loginType = loginType;
        }

        public String getOnline ()
        {
            return online;
        }

        public void setOnline (String online)
        {
            this.online = online;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getAvailableMinutes ()
        {
            return availableMinutes;
        }

        public void setAvailableMinutes (String availableMinutes)
        {
            this.availableMinutes = availableMinutes;
        }

        public String getPhoneNumber ()
        {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
        }

        public String getWalletBalance ()
        {
            return walletBalance;
        }

        public void setWalletBalance (String walletBalance)
        {
            this.walletBalance = walletBalance;
        }

        public String[] getSpecialities ()
        {
            return specialities;
        }

        public void setSpecialities (String[] specialities)
        {
            this.specialities = specialities;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getGender ()
        {
            return gender;
        }

        public void setGender (String gender)
        {
            this.gender = gender;
        }

        public String getFirstName ()
        {
            return firstName;
        }

        public void setFirstName (String firstName)
        {
            this.firstName = firstName;
        }

        public String getBonusProfileId ()
        {
            return bonusProfileId;
        }

        public void setBonusProfileId (String bonusProfileId)
        {
            this.bonusProfileId = bonusProfileId;
        }

        public String getRoleId ()
        {
            return roleId;
        }

        public void setRoleId (String roleId)
        {
            this.roleId = roleId;
        }

        public String getLastName ()
        {
            return lastName;
        }

        public void setLastName (String lastName)
        {
            this.lastName = lastName;
        }

        public String getEmotionId ()
        {
            return emotionId;
        }

        public void setEmotionId (String emotionId)
        {
            this.emotionId = emotionId;
        }

        public String getWebsiteURL ()
        {
            return websiteURL;
        }

        public void setWebsiteURL (String websiteURL)
        {
            this.websiteURL = websiteURL;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getCurrentlyAvailable ()
        {
            return currentlyAvailable;
        }

        public void setCurrentlyAvailable (String currentlyAvailable)
        {
            this.currentlyAvailable = currentlyAvailable;
        }

        public RoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (RoleModel roleModel)
        {
            this.roleModel = roleModel;
        }

        public Emotion getEmotion ()
        {
            return emotion;
        }

        public void setEmotion (Emotion emotion)
        {
            this.emotion = emotion;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String[] getCertification ()
        {
            return certification;
        }

        public void setCertification (String[] certification)
        {
            this.certification = certification;
        }

        public String getPrice ()
        {
            return price;
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getPin ()
        {
            return pin;
        }

        public void setPin (String pin)
        {
            this.pin = pin;
        }

        public String getNumberOfReviews ()
        {
            return numberOfReviews;
        }

        public void setNumberOfReviews (String numberOfReviews)
        {
            this.numberOfReviews = numberOfReviews;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", walletBalance = "+walletBalance+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", bonusProfileId = "+bonusProfileId+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", currentlyAvailable = "+currentlyAvailable+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", companyId = "+companyId+", rating = "+rating+"]";
        }
    }
*/



    public class RoleModel
    {
        private String updatedAt;

        private String id;

        private String createdAt;

        private String label;

        private String mainLabel;

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getLabel ()
        {
            return label;
        }

        public void setLabel (String label)
        {
            this.label = label;
        }

        public String getMainLabel ()
        {
            return mainLabel;
        }

        public void setMainLabel (String mainLabel)
        {
            this.mainLabel = mainLabel;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", createdAt = "+createdAt+", label = "+label+", mainLabel = "+mainLabel+"]";
        }
    }




    public class User
    {
        private String qualifications;

        private String qbid;

        private String[] education;

        private String loginType;

        private String online;

        private String id;

        private String availableMinutes;

        private String phoneNumber;

        private Double walletBalance;

        private String[] specialities;

        private String createdAt;

        private String description;

        private String gender;

        private String firstName;

        private String bonusProfileId;

        private String roleId;

        private String lastName;

        private String emotionId;

        private String websiteURL;

        private String image;

        private String currentlyAvailable;

        private RoleModel roleModel;

        private Emotion emotion;

        private String updatedAt;

        private String[] certification;

        private String price;

        private String pin;

        private String numberOfReviews;

        private String email;

        private String companyId;

        private String rating;

        public String getQualifications ()
        {
            return qualifications;
        }

        public void setQualifications (String qualifications)
        {
            this.qualifications = qualifications;
        }

        public String getQbid ()
        {
            return qbid;
        }

        public void setQbid (String qbid)
        {
            this.qbid = qbid;
        }

        public String[] getEducation ()
        {
            return education;
        }

        public void setEducation (String[] education)
        {
            this.education = education;
        }

        public String getLoginType ()
        {
            return loginType;
        }

        public void setLoginType (String loginType)
        {
            this.loginType = loginType;
        }

        public String getOnline ()
        {
            return online;
        }

        public void setOnline (String online)
        {
            this.online = online;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getAvailableMinutes ()
        {
            return availableMinutes;
        }

        public void setAvailableMinutes (String availableMinutes)
        {
            this.availableMinutes = availableMinutes;
        }

        public String getPhoneNumber ()
        {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
        }

        public Double getWalletBalance ()
        {
            return walletBalance;
        }

        public void setWalletBalance (Double walletBalance)
        {
            this.walletBalance = walletBalance;
        }

        public String[] getSpecialities ()
        {
            return specialities;
        }

        public void setSpecialities (String[] specialities)
        {
            this.specialities = specialities;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getGender ()
        {
            return gender;
        }

        public void setGender (String gender)
        {
            this.gender = gender;
        }

        public String getFirstName ()
        {
            return firstName;
        }

        public void setFirstName (String firstName)
        {
            this.firstName = firstName;
        }

        public String getBonusProfileId ()
        {
            return bonusProfileId;
        }

        public void setBonusProfileId (String bonusProfileId)
        {
            this.bonusProfileId = bonusProfileId;
        }

        public String getRoleId ()
        {
            return roleId;
        }

        public void setRoleId (String roleId)
        {
            this.roleId = roleId;
        }

        public String getLastName ()
        {
            return lastName;
        }

        public void setLastName (String lastName)
        {
            this.lastName = lastName;
        }

        public String getEmotionId ()
        {
            return emotionId;
        }

        public void setEmotionId (String emotionId)
        {
            this.emotionId = emotionId;
        }

        public String getWebsiteURL ()
        {
            return websiteURL;
        }

        public void setWebsiteURL (String websiteURL)
        {
            this.websiteURL = websiteURL;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getCurrentlyAvailable ()
        {
            return currentlyAvailable;
        }

        public void setCurrentlyAvailable (String currentlyAvailable)
        {
            this.currentlyAvailable = currentlyAvailable;
        }

        public RoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (RoleModel roleModel)
        {
            this.roleModel = roleModel;
        }

        public Emotion getEmotion ()
        {
            return emotion;
        }

        public void setEmotion (Emotion emotion)
        {
            this.emotion = emotion;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String[] getCertification ()
        {
            return certification;
        }

        public void setCertification (String[] certification)
        {
            this.certification = certification;
        }

        public String getPrice ()
        {
            return price;
        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getPin ()
        {
            return pin;
        }

        public void setPin (String pin)
        {
            this.pin = pin;
        }

        public String getNumberOfReviews ()
        {
            return numberOfReviews;
        }

        public void setNumberOfReviews (String numberOfReviews)
        {
            this.numberOfReviews = numberOfReviews;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", walletBalance = "+walletBalance+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", bonusProfileId = "+bonusProfileId+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", currentlyAvailable = "+currentlyAvailable+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", companyId = "+companyId+", rating = "+rating+"]";
        }
    }




    public class Emotion
    {
        private String updatedAt;

        private String id;

        private String title;

        private String createdAt;

        private String image;

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getTitle ()
        {
            return title;
        }

        public void setTitle (String title)
        {
            this.title = title;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", title = "+title+", createdAt = "+createdAt+", image = "+image+"]";
        }
    }





    public class ThisWeekStatistics
    {
        private String earnings;

        private String totalSessions;

        private String duration;

        private String spendings;

        private String availableMins;

        public String getEarnings ()
        {
            return earnings;
        }

        public void setEarnings (String earnings)
        {
            this.earnings = earnings;
        }

        public String getTotalSessions ()
        {
            return totalSessions;
        }

        public void setTotalSessions (String totalSessions)
        {
            this.totalSessions = totalSessions;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getSpendings ()
        {
            return spendings;
        }

        public void setSpendings (String spendings)
        {
            this.spendings = spendings;
        }

        public String getAvailableMins ()
        {
            return availableMins;
        }

        public void setAvailableMins (String availableMins)
        {
            this.availableMins = availableMins;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [earnings = "+earnings+", totalSessions = "+totalSessions+", duration = "+duration+", spendings = "+spendings+", availableMins = "+availableMins+"]";
        }
    }


    public class ThisMonthStatistics
    {
        private String earnings;

        private String totalSessions;

        private String duration;

        private String spendings;

        private String availableMins;

        public String getEarnings ()
        {
            return earnings;
        }

        public void setEarnings (String earnings)
        {
            this.earnings = earnings;
        }

        public String getTotalSessions ()
        {
            return totalSessions;
        }

        public void setTotalSessions (String totalSessions)
        {
            this.totalSessions = totalSessions;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getSpendings ()
        {
            return spendings;
        }

        public void setSpendings (String spendings)
        {
            this.spendings = spendings;
        }

        public String getAvailableMins ()
        {
            return availableMins;
        }

        public void setAvailableMins (String availableMins)
        {
            this.availableMins = availableMins;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [earnings = "+earnings+", totalSessions = "+totalSessions+", duration = "+duration+", spendings = "+spendings+", availableMins = "+availableMins+"]";
        }
    }



}