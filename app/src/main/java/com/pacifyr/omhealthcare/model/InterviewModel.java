package com.pacifyr.omhealthcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class InterviewModel implements Serializable {

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


    public class Result implements Serializable {

        @SerializedName("eventId")
        @Expose
        private String eventId;
        @SerializedName("interviewStatus")
        @Expose
        private String interviewStatus;
        @SerializedName("dressedForInterview")
        @Expose
        private Boolean dressedForInterview;
        @SerializedName("freshSeshCompleted")
        @Expose
        private Boolean freshSeshCompleted;
        @SerializedName("properGreeting")
        @Expose
        private Boolean properGreeting;
        @SerializedName("finalOutcome")
        @Expose
        private Object finalOutcome;
        @SerializedName("hadCopyOfResume")
        @Expose
        private Boolean hadCopyOfResume;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("resumeZoneCompleted")
        @Expose
        private Boolean resumeZoneCompleted;
        @SerializedName("elevatedIntroZoneCompleted")
        @Expose
        private Boolean elevatedIntroZoneCompleted;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("employerId")
        @Expose
        private Object employerId;
        @SerializedName("mockInterviewCompleted")
        @Expose
        private Boolean mockInterviewCompleted;
        @SerializedName("employer")
        @Expose
        private ModelEvent.Employer employer;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("dressForSuccessCompleted")
        @Expose
        private Boolean dressForSuccessCompleted;
        @SerializedName("event")
        @Expose
        private ModelEvent event;
        @SerializedName("user")
        @Expose
        private Object user;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("employerPosition")
        @Expose
        private EmployerPosition employerPosition;

        public EmployerPosition getEmployerPosition() {
            return employerPosition;
        }

        public void setEmployerPosition(EmployerPosition employerPosition) {
            this.employerPosition = employerPosition;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getInterviewStatus() {
            return interviewStatus;
        }

        public void setInterviewStatus(String interviewStatus) {
            this.interviewStatus = interviewStatus;
        }

        public Boolean getDressedForInterview() {
            return dressedForInterview;
        }

        public void setDressedForInterview(Boolean dressedForInterview) {
            this.dressedForInterview = dressedForInterview;
        }

        public Boolean getFreshSeshCompleted() {
            return freshSeshCompleted;
        }

        public void setFreshSeshCompleted(Boolean freshSeshCompleted) {
            this.freshSeshCompleted = freshSeshCompleted;
        }

        public Boolean getProperGreeting() {
            return properGreeting;
        }

        public void setProperGreeting(Boolean properGreeting) {
            this.properGreeting = properGreeting;
        }

        public Object getFinalOutcome() {
            return finalOutcome;
        }

        public void setFinalOutcome(Object finalOutcome) {
            this.finalOutcome = finalOutcome;
        }

        public Boolean getHadCopyOfResume() {
            return hadCopyOfResume;
        }

        public void setHadCopyOfResume(Boolean hadCopyOfResume) {
            this.hadCopyOfResume = hadCopyOfResume;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Boolean getResumeZoneCompleted() {
            return resumeZoneCompleted;
        }

        public void setResumeZoneCompleted(Boolean resumeZoneCompleted) {
            this.resumeZoneCompleted = resumeZoneCompleted;
        }

        public Boolean getElevatedIntroZoneCompleted() {
            return elevatedIntroZoneCompleted;
        }

        public void setElevatedIntroZoneCompleted(Boolean elevatedIntroZoneCompleted) {
            this.elevatedIntroZoneCompleted = elevatedIntroZoneCompleted;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Object getEmployerId() {
            return employerId;
        }

        public void setEmployerId(Object employerId) {
            this.employerId = employerId;
        }

        public Boolean getMockInterviewCompleted() {
            return mockInterviewCompleted;
        }

        public void setMockInterviewCompleted(Boolean mockInterviewCompleted) {
            this.mockInterviewCompleted = mockInterviewCompleted;
        }

        public ModelEvent.Employer getEmployer() {
            return employer;
        }

        public void setEmployer(ModelEvent.Employer employer) {
            this.employer = employer;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Boolean getDressForSuccessCompleted() {
            return dressForSuccessCompleted;
        }

        public void setDressForSuccessCompleted(Boolean dressForSuccessCompleted) {
            this.dressForSuccessCompleted = dressForSuccessCompleted;
        }

        public ModelEvent getEvent() {
            return event;
        }

        public void setEvent(ModelEvent event) {
            this.event = event;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }


    public class EmployerPosition implements Serializable {

        @SerializedName("benefits")
        @Expose
        private List<String> benefits = null;
        @SerializedName("qualifications")
        @Expose
        private List<String> qualifications = null;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("website")
        @Expose
        private String website;
        @SerializedName("employerId")
        @Expose
        private String employerId;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("numberOfPositions")
        @Expose
        private String numberOfPositions;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("salary")
        @Expose
        private String salary;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public List<String> getBenefits() {
            return benefits;
        }

        public void setBenefits(List<String> benefits) {
            this.benefits = benefits;
        }

        public List<String> getQualifications() {
            return qualifications;
        }

        public void setQualifications(List<String> qualifications) {
            this.qualifications = qualifications;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getEmployerId() {
            return employerId;
        }

        public void setEmployerId(String employerId) {
            this.employerId = employerId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNumberOfPositions() {
            return numberOfPositions;
        }

        public void setNumberOfPositions(String numberOfPositions) {
            this.numberOfPositions = numberOfPositions;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSalary() {
            return salary;
        }

        public void setSalary(String salary) {
            this.salary = salary;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }

}
