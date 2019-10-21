package com.pacifyr.omhealthcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ModelEvent implements Serializable {
    @SerializedName("total_count")
    @Expose
    private Integer totalCount;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("employers")
    @Expose
    private List<Employer> employers = null;

    @SerializedName("positions")
    @Expose
    private List<Position> positions = null;
    @SerializedName("attendingUserIds")
    @Expose
    private List<String> attendingUserIds = null;

    @SerializedName("checkinUserIds")
    @Expose
    private List<String> checkinUserIds = null;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;


    @SerializedName("location")
    @Expose
    private String location;

    @SerializedName("date")
    @Expose
    private List<String> date = null;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private String id;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDate() {
        return date;
    }

    public void setDate(List<String> date) {
        this.date = date;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

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

    public List<Employer> getEmployers() {
        return employers;
    }

    public void setEmployers(List<Employer> employers) {
        this.employers = employers;
    }

    public List<String> getAttendingUserIds() {
        return attendingUserIds;
    }

    public void setAttendingUserIds(List<String> attendingUserIds) {
        this.attendingUserIds = attendingUserIds;
    }

    public List<String> getCheckinUserIds() {
        return checkinUserIds;
    }

    public void setCheckinUserIds(List<String> checkinUserIds) {
        this.checkinUserIds = checkinUserIds;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public class Result implements Serializable {

        @SerializedName("date")
        @Expose
        private List<String> date = null;
        @SerializedName("image")
        @Expose
        private String image;

        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("schedule")
        @Expose
        private String schedule;
        @SerializedName("companyId")
        @Expose
        private String companyId;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("startTime")
        @Expose
        private String startTime;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("endTime")
        @Expose
        private String endTime;
        @SerializedName("employerIds")
        @Expose
        private List<String> employerIds = null;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        @SerializedName("event")
        @Expose
        private Event event;

        public List<String> getDate() {
            return date;
        }

        public void setDate(List<String> date) {
            this.date = date;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }


        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getSchedule() {
            return schedule;
        }

        public void setSchedule(String schedule) {
            this.schedule = schedule;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<String> getEmployerIds() {
            return employerIds;
        }

        public void setEmployerIds(List<String> employerIds) {
            this.employerIds = employerIds;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }


        public Event getEvent() {
            return event;
        }

        public void setEvent(Event event) {
            this.event = event;
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


    public class Position implements Serializable {

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





/*



    private String date;
    private String title;
    private String description;
    private String address;
    private String image;
    private String opening;

    public ModelEvent(String date, String title, String description, String address,String image) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.address = address;
        this.image=image;
    }

    public ModelEvent(String title) {
        this.title = title;
    }

    public ModelEvent(String title,String opening,String description) {
        this.title = title;
        this.opening=opening;
        this.description=description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


*/


}
