package com.pacifyrapp.utilitylibrary.model.pacifyr;

import com.pacifyrapp.utilitylibrary.model.Company;
import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Samsad on 31/7/17.
 */

public class ModelUserProfile
{

    public void setEducation (ArrayList<Education> list)
    {
        if(list!=null)
            this.education=list.toArray( (new Education[list.size()]));
    }

    public ArrayList<Education> getEducationList() {
        ArrayList<Education> arrayList = null;
        try {
            if(education != null) {
                arrayList = new ArrayList<Education>(Arrays.asList(education));

            }
        } catch (Exception e){
            arrayList = null;
        }
        return arrayList;
    }


    public void setCertification (ArrayList<Certification> list)
    {
        if(list!=null)
            this.certification=list.toArray( (new Certification[list.size()]));
    }

    public ArrayList<Certification> getCertificationList() {
        ArrayList<Certification> arrayList = null;
        try {
            if(certification != null) {
                arrayList = new ArrayList<Certification>(Arrays.asList(certification));
            }
        } catch (Exception e){
            arrayList = null;
        }
        return arrayList;
    }



    private String dateOfBirth;

    private String qualifications;

    private Long qbid;

    private String state;

    private String maritalStatus;

    private Education[] education;

    private String loginType;

    private Boolean online;

    private String id;

    private long availableMinutes;

    private String phoneNumber;

    private int walletBalance;

    private String[] specialities;

    private String createdAt;

    private String description;

    private String gender;

    private String firstName;

    private String bonusProfileId;

    private String roleId;

    private String selectedSpeciality;

    private String selectedSpecialityCategory;

    private String lastName;

    private String emotionId;

    private String websiteURL;

    private String image;

    private String termsAccepted;

    private boolean currentlyAvailable;

    private RoleModel roleModel;

    private MEmotion emotion;

    private String country;

    private String updatedAt;

    private Certification[] certification;

    private String price;

    private String pin;

    private String numberOfReviews;

    private String email;

    private String companyId;

    private String rating;

    //for leadersup
    private String zipCode;
    private String getPaidForQuickSurveys;
    private String refferedOrganisation;
    private String region;
    private String referredServices;
    private String race;
    private String educationStatus;
    private String schoolName;
    private String address1;

    private Company company;

    private boolean enableScheduling;
    public boolean isEnableScheduling() {
        return enableScheduling;
    }

    public void setEnableScheduling(boolean enableScheduling) {
        this.enableScheduling = enableScheduling;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getGetPaidForQuickSurveys() {
        return getPaidForQuickSurveys;
    }

    public void setGetPaidForQuickSurveys(String getPaidForQuickSurveys) {
        this.getPaidForQuickSurveys = getPaidForQuickSurveys;
    }

    public String getRefferedOrganisation() {
        return refferedOrganisation;
    }

    public void setRefferedOrganisation(String refferedOrganisation) {
        this.refferedOrganisation = refferedOrganisation;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getReferredServices() {
        return referredServices;
    }

    public void setReferredServices(String referredServices) {
        this.referredServices = referredServices;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getEducationStatus() {
        return educationStatus;
    }

    public void setEducationStatus(String educationStatus) {
        this.educationStatus = educationStatus;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    private boolean sessionChargeable;

    public boolean isSessionChargeable() {
        return sessionChargeable;
    }

    public void setSessionChargeable(boolean sessionChargeable) {
        this.sessionChargeable = sessionChargeable;
    }

    public String getQualifications ()
    {
        return qualifications;
    }

    public void setQualifications (String qualifications)
    {
        this.qualifications = qualifications;
    }

    public Long getQbid ()
    {
        return qbid;
    }

    public void setQbid (Long qbid)
    {
        this.qbid = qbid;
    }

    public Education[] getEducation ()
    {
        return education;
    }

    public void setEducation (Education[] education)
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

    public Boolean getOnline ()
    {
        return online;
    }

    public void setOnline (Boolean online)
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

    public long getAvailableMinutes ()
    {
        return availableMinutes;
    }

    public void setAvailableMinutes (long availableMinutes)
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

    public int getWalletBalance ()
    {
        return walletBalance;
    }

    public void setWalletBalance (int walletBalance)
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

    public String getName ()
    {
        if (lastName.equals(""))
            return firstName.trim();
        else
            return firstName.trim() + " " + lastName.trim();
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

/*  public String getName ()
    {
        return firstName+" "+lastName;
    }*/


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
    private String reasonForTheCall;
    public String getReasonForTheCall ()
    {
        return reasonForTheCall;
    }

    public void setReasonForTheCall (String reasonForTheCall)
    {
        this.reasonForTheCall = reasonForTheCall;
    }


    public Boolean isValid(String text) {
        if (text != null) return !text.trim().equalsIgnoreCase("");
        return false;

    }

    public String getImageUrl ()
    {
        String image_url=null;
        if (isValid(image))
            if(image.contains("http")||image.contains("https"))
                image_url=image;
            else
            image_url= UtilityNetworkService.GLOBAL_IMAGE_URL + image;
        return image_url;
    }
    public void setImage (String image)
    {
        this.image = image;
    }

    public RoleModel getRoleModel ()
    {
        return roleModel;
    }

    public void setRoleModel (RoleModel roleModel)
    {
        this.roleModel = roleModel;
    }

    public MEmotion getEmotion ()
    {
        return emotion;
    }

    public void setEmotion (MEmotion emotion)
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

    public Certification[] getCertification ()
    {
        return certification;
    }

    public void setCertification (Certification[] certification)
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
        return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", walletBalance = "+walletBalance+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", companyId = "+companyId+", rating = "+rating+"]";
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getBonusProfileId() {
        return bonusProfileId;
    }

    public void setBonusProfileId(String bonusProfileId) {
        this.bonusProfileId = bonusProfileId;
    }

    public String getSelectedSpeciality() {
        return selectedSpeciality;
    }

    public void setSelectedSpeciality(String selectedSpeciality) {
        this.selectedSpeciality = selectedSpeciality;
    }

    public String getSelectedSpecialityCategory() {
        return selectedSpecialityCategory;
    }

    public void setSelectedSpecialityCategory(String selectedSpecialityCategory) {
        this.selectedSpecialityCategory = selectedSpecialityCategory;
    }

    public String getTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(String termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public boolean getCurrentlyAvailable() {
        return currentlyAvailable;
    }

    public void setCurrentlyAvailable(boolean currentlyAvailable) {
        this.currentlyAvailable = currentlyAvailable;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public class RoleModel
    {
        private String updatedAt;

        private String id;

        private String createdAt;

        private String label;

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

        @Override
        public String toString()
        {
            return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", createdAt = "+createdAt+", label = "+label+"]";
        }
    }

 /*   public class Emotion
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
    }*/







    public class Education
    {
        private String id;

        private String title;

        private String duration;

        private String order;

        private String location;

        private String userId;

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

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getOrder ()
        {
            return order;
        }

        public void setOrder (String order)
        {
            this.order = order;
        }

        public String getLocation ()
        {
            return location;
        }

        public void setLocation (String location)
        {
            this.location = location;
        }

        public String getUserId ()
        {
            return userId;
        }

        public void setUserId (String userId)
        {
            this.userId = userId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [id = "+id+", title = "+title+", duration = "+duration+", order = "+order+", location = "+location+", userId = "+userId+"]";
        }
    }


}
