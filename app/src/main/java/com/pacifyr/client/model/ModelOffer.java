package com.pacifyr.client.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelOffer implements Serializable {

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
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("employerId")
        @Expose
        private String employerId;
        @SerializedName("employerPosition")
        @Expose
        private EmployerPosition employerPosition;
        @SerializedName("employerPositionId")
        @Expose
        private String employerPositionId;
        @SerializedName("employer")
        @Expose
        private Employer employer;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("event")
        @Expose
        private Object event;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("user")
        @Expose
        private Object user;
        @SerializedName("startDate")
        @Expose
        private String startDate;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getEmployerId() {
            return employerId;
        }

        public void setEmployerId(String employerId) {
            this.employerId = employerId;
        }

        public EmployerPosition getEmployerPosition() {
            return employerPosition;
        }

        public void setEmployerPosition(EmployerPosition employerPosition) {
            this.employerPosition = employerPosition;
        }

        public String getEmployerPositionId() {
            return employerPositionId;
        }

        public void setEmployerPositionId(String employerPositionId) {
            this.employerPositionId = employerPositionId;
        }

        public Employer getEmployer() {
            return employer;
        }

        public void setEmployer(Employer employer) {
            this.employer = employer;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getEvent() {
            return event;
        }

        public void setEvent(Object event) {
            this.event = event;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Object getUser() {
            return user;
        }

        public void setUser(Object user) {
            this.user = user;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
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


    public class Employer implements Serializable {

        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("primaryPhone")
        @Expose
        private String primaryPhone;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("checkedIn")
        @Expose
        private Boolean checkedIn;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("positions")
        @Expose
        private Object positions;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrimaryPhone() {
            return primaryPhone;
        }

        public void setPrimaryPhone(String primaryPhone) {
            this.primaryPhone = primaryPhone;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Boolean getCheckedIn() {
            return checkedIn;
        }

        public void setCheckedIn(Boolean checkedIn) {
            this.checkedIn = checkedIn;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getPositions() {
            return positions;
        }

        public void setPositions(Object positions) {
            this.positions = positions;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

    }
}
