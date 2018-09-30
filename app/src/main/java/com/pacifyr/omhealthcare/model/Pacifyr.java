package com.pacifyr.omhealthcare.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pacifyr {

    @SerializedName("chatSubscription")
    @Expose
    private Object chatSubscription;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("stripeAccountId")
    @Expose
    private Object stripeAccountId;
    @SerializedName("education")
    @Expose
    private List<Object> education = null;
    @SerializedName("numberOfReviews")
    @Expose
    private Integer numberOfReviews;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("freeChatAvailable")
    @Expose
    private Boolean freeChatAvailable;
    @SerializedName("employedToCompany")
    @Expose
    private List<Object> employedToCompany = null;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("qualifications")
    @Expose
    private Object qualifications;
    @SerializedName("pin")
    @Expose
    private Object pin;
    @SerializedName("walletBalance")
    @Expose
    private Integer walletBalance;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("state")
    @Expose
    private Object state;
    @SerializedName("availableMinutes")
    @Expose
    private Integer availableMinutes;
    @SerializedName("specialities")
    @Expose
    private List<String> specialities = null;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("deviceType")
    @Expose
    private Object deviceType;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("accountType")
    @Expose
    private Object accountType;
    @SerializedName("certification")
    @Expose
    private List<Object> certification = null;
    @SerializedName("sessionChargeable")
    @Expose
    private Boolean sessionChargeable;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("companyId")
    @Expose
    private Object companyId;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("numberOfDeclinedCalls")
    @Expose
    private Integer numberOfDeclinedCalls;
    @SerializedName("maritalStatus")
    @Expose
    private Object maritalStatus;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("gender")
    @Expose
    private Object gender;
    @SerializedName("loginType")
    @Expose
    private Object loginType;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("currentlyAvailable")
    @Expose
    private Boolean currentlyAvailable;
    @SerializedName("chatSubscriptionRequired")
    @Expose
    private Boolean chatSubscriptionRequired;
    @SerializedName("bonusProfileId")
    @Expose
    private Object bonusProfileId;
    @SerializedName("roleModel")
    @Expose
    private RoleModel roleModel;
    @SerializedName("websiteURL")
    @Expose
    private Object websiteURL;
    @SerializedName("qbid")
    @Expose
    private Integer qbid;
    @SerializedName("company")
    @Expose
    private Object company;
    @SerializedName("selectedSpeciality")
    @Expose
    private Object selectedSpeciality;
    @SerializedName("selectedSpecialityCategory")
    @Expose
    private Object selectedSpecialityCategory;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("numberOfMissedCalls")
    @Expose
    private Integer numberOfMissedCalls;
    @SerializedName("roleId")
    @Expose
    private String roleId;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("emotion")
    @Expose
    private Object emotion;
    @SerializedName("termsAccepted")
    @Expose
    private Boolean termsAccepted;
    @SerializedName("emotionId")
    @Expose
    private String emotionId;
    @SerializedName("priceForChat")
    @Expose
    private Object priceForChat;
    @SerializedName("online")
    @Expose
    private Boolean online;
    @SerializedName("age")
    @Expose
    private Object age;

    public Object getChatSubscription() {
        return chatSubscription;
    }

    public void setChatSubscription(Object chatSubscription) {
        this.chatSubscription = chatSubscription;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Object getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(Object stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public List<Object> getEducation() {
        return education;
    }

    public void setEducation(List<Object> education) {
        this.education = education;
    }

    public Integer getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(Integer numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getFreeChatAvailable() {
        return freeChatAvailable;
    }

    public void setFreeChatAvailable(Boolean freeChatAvailable) {
        this.freeChatAvailable = freeChatAvailable;
    }

    public List<Object> getEmployedToCompany() {
        return employedToCompany;
    }

    public void setEmployedToCompany(List<Object> employedToCompany) {
        this.employedToCompany = employedToCompany;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getQualifications() {
        return qualifications;
    }

    public void setQualifications(Object qualifications) {
        this.qualifications = qualifications;
    }

    public Object getPin() {
        return pin;
    }

    public void setPin(Object pin) {
        this.pin = pin;
    }

    public Integer getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Integer walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Integer getAvailableMinutes() {
        return availableMinutes;
    }

    public void setAvailableMinutes(Integer availableMinutes) {
        this.availableMinutes = availableMinutes;
    }

    public List<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<String> specialities) {
        this.specialities = specialities;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Object deviceType) {
        this.deviceType = deviceType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Object getAccountType() {
        return accountType;
    }

    public void setAccountType(Object accountType) {
        this.accountType = accountType;
    }

    public List<Object> getCertification() {
        return certification;
    }

    public void setCertification(List<Object> certification) {
        this.certification = certification;
    }

    public Boolean getSessionChargeable() {
        return sessionChargeable;
    }

    public void setSessionChargeable(Boolean sessionChargeable) {
        this.sessionChargeable = sessionChargeable;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Object getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Object companyId) {
        this.companyId = companyId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getNumberOfDeclinedCalls() {
        return numberOfDeclinedCalls;
    }

    public void setNumberOfDeclinedCalls(Integer numberOfDeclinedCalls) {
        this.numberOfDeclinedCalls = numberOfDeclinedCalls;
    }

    public Object getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(Object maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Object getGender() {
        return gender;
    }

    public void setGender(Object gender) {
        this.gender = gender;
    }

    public Object getLoginType() {
        return loginType;
    }

    public void setLoginType(Object loginType) {
        this.loginType = loginType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCurrentlyAvailable() {
        return currentlyAvailable;
    }

    public void setCurrentlyAvailable(Boolean currentlyAvailable) {
        this.currentlyAvailable = currentlyAvailable;
    }

    public Boolean getChatSubscriptionRequired() {
        return chatSubscriptionRequired;
    }

    public void setChatSubscriptionRequired(Boolean chatSubscriptionRequired) {
        this.chatSubscriptionRequired = chatSubscriptionRequired;
    }

    public Object getBonusProfileId() {
        return bonusProfileId;
    }

    public void setBonusProfileId(Object bonusProfileId) {
        this.bonusProfileId = bonusProfileId;
    }

    public RoleModel getRoleModel() {
        return roleModel;
    }

    public void setRoleModel(RoleModel roleModel) {
        this.roleModel = roleModel;
    }

    public Object getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(Object websiteURL) {
        this.websiteURL = websiteURL;
    }

    public Integer getQbid() {
        return qbid;
    }

    public void setQbid(Integer qbid) {
        this.qbid = qbid;
    }

    public Object getCompany() {
        return company;
    }

    public void setCompany(Object company) {
        this.company = company;
    }

    public Object getSelectedSpeciality() {
        return selectedSpeciality;
    }

    public void setSelectedSpeciality(Object selectedSpeciality) {
        this.selectedSpeciality = selectedSpeciality;
    }

    public Object getSelectedSpecialityCategory() {
        return selectedSpecialityCategory;
    }

    public void setSelectedSpecialityCategory(Object selectedSpecialityCategory) {
        this.selectedSpecialityCategory = selectedSpecialityCategory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumberOfMissedCalls() {
        return numberOfMissedCalls;
    }

    public void setNumberOfMissedCalls(Integer numberOfMissedCalls) {
        this.numberOfMissedCalls = numberOfMissedCalls;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Object getEmotion() {
        return emotion;
    }

    public void setEmotion(Object emotion) {
        this.emotion = emotion;
    }

    public Boolean getTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(Boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public String getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(String emotionId) {
        this.emotionId = emotionId;
    }

    public Object getPriceForChat() {
        return priceForChat;
    }

    public void setPriceForChat(Object priceForChat) {
        this.priceForChat = priceForChat;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
        this.age = age;
    }

}