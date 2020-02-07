package com.pacifyrapp.utilitylibrary.model.pacifyr;

import android.annotation.SuppressLint;

import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mani-Ceino on 12/14/2017.
 */

public class MUser implements Serializable{


        private String dateOfBirth;

        private String qualifications;

        private String numberOfMissedCalls;

        private String[] employedToCompany;

        private String state;

        private long qbid;

        private String maritalStatus;

        private Education[] education;

        private String loginType;

        private boolean online;

        private String id;

        private long availableMinutes;

        private String phoneNumber;

        private ChatSubscription chatSubscription;


        private String walletBalance;

        private String createdAt;

        private String description;

        private String gender;

        private String firstName;

        private String bonusProfileId;

        private String roleId;

        private String selectedSpeciality;
    private String selectedSpecialityCategory;

        private String lastName;

        private String[] specialities;

        private String emotionId;

        private String stripeAccountId;

        private String websiteURL;

        private String image;

        private boolean termsAccepted;

        private boolean chatSubscriptionRequired;

        private boolean currentlyAvailable;

        private boolean chatEnabled=false;

        private MRoleModel roleModel;

        private boolean sessionChargeable;

        private MEmotion emotion;

        private String country;

        private String updatedAt;

        private Certification[] certification;

        private String price;

        private String priceForChat;

        private String numberOfReviews;

        private String pin;

        private String email;

        private String companyId;

        private String rating;

        private String reasonForTheCall;

    private String zipCode;
    private boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @SerializedName("forwardToPacifyrId")
    @Expose
    private String forwardToPacifyrId;

    @SerializedName("forwardToPacifyr")
    @Expose
    private MUser forwardToPacifyr;

    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getSelectedSpecialityCategory() {
        return selectedSpecialityCategory;
    }

    public void setSelectedSpecialityCategory(String selectedSpecialityCategory) {
        this.selectedSpecialityCategory = selectedSpecialityCategory;
    }

    public String getForwardToPacifyrId() {
        return forwardToPacifyrId;
    }

    public void setForwardToPacifyrId(String forwardToPacifyrId) {
        this.forwardToPacifyrId = forwardToPacifyrId;
    }

    public MUser getForwardToPacifyr() {
        return forwardToPacifyr;
    }

    public void setForwardToPacifyr(MUser forwardToPacifyr) {
        this.forwardToPacifyr = forwardToPacifyr;
    }

        public String getReasonForTheCall ()
    {
        return reasonForTheCall;
    }

    public void setReasonForTheCall (String reasonForTheCall)
    {
        this.reasonForTheCall = reasonForTheCall;
    }



    public boolean isChatEnabled() {
        return chatEnabled;
    }

    public void setChatEnabled(boolean chatEnabled) {
        this.chatEnabled = chatEnabled;
    }
/*

       private Education[] education;
       private Certification[] certification;
    public Certification[] getCertification ()
    {
        return certification;
    }

    public void setCertification (Certification[] certification)
    {
        this.certification = certification;
    }


    public void setEducation (ArrayList<ModelUserProfile.Education> list)
    {
        if(list!=null)
            this.education=list.toArray( Education[list.size()]));
    }

    public ArrayList<ModelUserProfile.Education> getEducationList() {
        ArrayList<ModelUserProfile.Education> arrayList = null;
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
*/


    public String getDateOfBirth ()
    {
        return dateOfBirth;
    }

        public void setDateOfBirth (String dateOfBirth)
        {
            this.dateOfBirth = dateOfBirth;
        }

        public String getQualifications ()
    {
        return qualifications;
    }

        public void setQualifications (String qualifications)
        {
            this.qualifications = qualifications;
        }

        public String[] getEmployedToCompany ()
        {
            return employedToCompany;
        }

        public void setEmployedToCompany (String[] employedToCompany)
        {
            this.employedToCompany = employedToCompany;
        }

        public String getState ()
    {
        return state;
    }

        public void setState (String state)
        {
            this.state = state;
        }

        public long getQbid ()
        {
            return qbid;
        }

        public void setQbid (long qbid)
        {
            this.qbid = qbid;
        }

        public String getMaritalStatus ()
    {
        return maritalStatus;
    }

        public void setMaritalStatus (String maritalStatus)
        {
            this.maritalStatus = maritalStatus;
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

        public boolean getOnline ()
        {
            return online;
        }

        public void setOnline (boolean online)
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

        public String[] getSpecialities ()
    {
        return specialities;
    }

        public void setSpecialities (String[] specialities)
        {
            this.specialities = specialities;
        }

        public String getWalletBalance ()
        {
            return walletBalance;
        }

        public void setWalletBalance (String walletBalance)
        {
            this.walletBalance = walletBalance;
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
    public String getName ()
    {
        return firstName.trim() + " " + lastName.trim();
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

        public String getSelectedSpeciality ()
        {
            return selectedSpeciality;
        }

        public void setSelectedSpeciality (String selectedSpeciality)
        {
            this.selectedSpeciality = selectedSpeciality;
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

        public String getImageUrl ()
        {
            String url="";
            try {
                if(image!=null)
                    if(image.contains("http")||image.contains("https"))
                        url=image;
                      else
                     url= UtilityNetworkService.GLOBAL_IMAGE_URL+image;
            }catch (Exception e)
            {
                url="";
            }
            return url;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public boolean isTermsAccepted ()
        {
            return termsAccepted;
        }

        public void setTermsAccepted (boolean termsAccepted)
        {
            this.termsAccepted = termsAccepted;
        }

        public boolean getCurrentlyAvailable ()
        {
            return currentlyAvailable;
        }

        public boolean isReadyToCall()
        {
            boolean isReady=false;
            try {
                if(online)
                {
                    if(currentlyAvailable)
                        isReady=true;
                }


            }catch (Exception e)
            {
                isReady=false;
            }

            return isReady;
        }


        public void setCurrentlyAvailable (boolean currentlyAvailable)
        {
            this.currentlyAvailable = currentlyAvailable;
        }

        public MRoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (MRoleModel roleModel)
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

        public String getCountry ()
    {
        return country;
    }

        public void setCountry (String country)
        {
            this.country = country;
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

        @SuppressLint("DefaultLocale")
        public String getPriceWithSign ()
        {
            if (country.equals("India"))
                return "₹ " + String.format("%.2f", Double.parseDouble(price));
            else
                return "$ " + String.format("%.2f", Double.parseDouble(price));

        }

        public void setPrice (String price)
        {
            this.price = price;
        }

        public String getNumberOfReviews ()
        {
            return numberOfReviews;
        }

        public void setNumberOfReviews (String numberOfReviews)
        {
            this.numberOfReviews = numberOfReviews;
        }

        public String getPin ()
    {
        return pin;
    }

        public void setPin (String pin)
        {
            this.pin = pin;
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
            return "";
            //return "ClassPojo [dateOfBirth = "+dateOfBirth+", qualifications = "+qualifications+", employedToCompany = "+employedToCompany+", state = "+state+", qbid = "+qbid+", maritalStatus = "+maritalStatus+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", specialities = "+specialities+", walletBalance = "+walletBalance+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", bonusProfileId = "+bonusProfileId+", roleId = "+roleId+", selectedSpeciality = "+selectedSpeciality+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", termsAccepted = "+termsAccepted+", currentlyAvailable = "+currentlyAvailable+", roleModel = "+roleModel+", emotion = "+emotion+", country = "+country+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", numberOfReviews = "+numberOfReviews+", pin = "+pin+", email = "+email+", companyId = "+companyId+", rating = "+rating+"]";
        }


    public boolean isSessionChargeable() {
        return sessionChargeable;
    }

    public void setSessionChargeable(boolean sessionChargeable) {
        this.sessionChargeable = sessionChargeable;
    }

    public String getNumberOfMissedCalls() {
        return numberOfMissedCalls;
    }

    public Integer getNumberOfMissedCallsInt() {
        return Integer.valueOf(numberOfMissedCalls);
    }

    public void setNumberOfMissedCalls(String numberOfMissedCalls) {
        this.numberOfMissedCalls = numberOfMissedCalls;
    }

    public String getPriceForChat() {
        if (priceForChat==null)
        {
            priceForChat="0";
        }
        return priceForChat;
    }

    public void setPriceForChat(String priceForChat) {
        this.priceForChat = priceForChat;
    }

    public boolean isChatSubscriptionRequired() {
        return chatSubscriptionRequired;
    }

    public void setChatSubscriptionRequired(boolean chatSubscriptionRequired) {
        this.chatSubscriptionRequired = chatSubscriptionRequired;
    }

    public ChatSubscription getChatSubscription() {
        return chatSubscription;
    }

    public void setChatSubscription(ChatSubscription chatSubscription) {
        this.chatSubscription = chatSubscription;
    }


    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    @SuppressLint("DefaultLocale")
    public String getPriceIninute() {
        double priceINHours = Double.parseDouble(price);
        if (country.equals("India"))

            return "₹ " + String.format("%.2f", priceINHours/60);
        else
            return "$ " + String.format("%.2f", priceINHours/60);


    }
}
