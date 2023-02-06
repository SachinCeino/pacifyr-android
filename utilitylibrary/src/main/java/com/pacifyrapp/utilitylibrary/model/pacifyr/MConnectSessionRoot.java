package com.pacifyrapp.utilitylibrary.model.pacifyr;

/**
 * Created by developer on 12/21/2017.
 */

public class MConnectSessionRoot {

    private String message;

    private String status;

    private Session session;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public Session getSession ()
    {
        return session;
    }

    public void setSession (Session session)
    {
        this.session = session;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", session = "+session+"]";
    }


    public class Session
    {

        private String id;
        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        private MUser pacifyr;
        private MUser user;
        public MUser getPacifyr ()
        {
            return pacifyr;
        }

        public void setPacifyr (MUser pacifyr)
        {
            this.pacifyr = pacifyr;
        }

        public MUser getUser ()
        {
            return user;
        }

        public void setUser (MUser user)
        {
            this.user = user;
        }

     /*   private String sessionCost;

        private String connected;

        private String endTime;



        private String startTime;

        private String declined;

        private String createdAt;

        private String userId;

        private String afterSessionCustomerEmotionId;

        private String customerRating;

        private String reasonForTheCall;

        private String sessionRatings;

        private String creditCardCharges;

        private String sessionType;

        private String updatedAt;

        private String pacifyrId;

        private String afterSessionPacifyrEmotionId;

        private String duration;

        private String pacifyrEarnings;

        private String stopped;

        private String anonymousCall;



        private String scheduledSessionId;

        private String missed;


      *//*  private null aggregationSessionId;*//*

        public String getSessionCost ()
        {
            return sessionCost;
        }

        public void setSessionCost (String sessionCost)
        {
            this.sessionCost = sessionCost;
        }

        public String getConnected ()
        {
            return connected;
        }

        public void setConnected (String connected)
        {
            this.connected = connected;
        }

        public String getEndTime ()
    {
        return endTime;
    }

        public void setEndTime (String endTime)
        {
            this.endTime = endTime;
        }


        public String getStartTime ()
        {
            return startTime;
        }

        public void setStartTime (String startTime)
        {
            this.startTime = startTime;
        }

        public String getDeclined ()
        {
            return declined;
        }

        public void setDeclined (String declined)
        {
            this.declined = declined;
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

        public String getAfterSessionCustomerEmotionId ()
    {
        return afterSessionCustomerEmotionId;
    }

        public void setAfterSessionCustomerEmotionId (String afterSessionCustomerEmotionId)
        {
            this.afterSessionCustomerEmotionId = afterSessionCustomerEmotionId;
        }

        public String getCustomerRating ()
        {
            return customerRating;
        }

        public void setCustomerRating (String customerRating)
        {
            this.customerRating = customerRating;
        }

        public String getReasonForTheCall ()
    {
        return reasonForTheCall;
    }

        public void setReasonForTheCall (String reasonForTheCall)
        {
            this.reasonForTheCall = reasonForTheCall;
        }

        public String getSessionRatings ()
    {
        return sessionRatings;
    }

        public void setSessionRatings (String sessionRatings)
        {
            this.sessionRatings = sessionRatings;
        }

        public String getCreditCardCharges ()
        {
            return creditCardCharges;
        }

        public void setCreditCardCharges (String creditCardCharges)
        {
            this.creditCardCharges = creditCardCharges;
        }

        public String getSessionType ()
        {
            return sessionType;
        }

        public void setSessionType (String sessionType)
        {
            this.sessionType = sessionType;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
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

        public String getAnonymousCall ()
        {
            return anonymousCall;
        }

        public void setAnonymousCall (String anonymousCall)
        {
            this.anonymousCall = anonymousCall;
        }

        public User getPacifyr ()
        {
            return pacifyr;
        }

        public void setPacifyr (User pacifyr)
        {
            this.pacifyr = pacifyr;
        }

        public String getScheduledSessionId ()
    {
        return scheduledSessionId;
    }

        public void setScheduledSessionId (String scheduledSessionId)
        {
            this.scheduledSessionId = scheduledSessionId;
        }

        public String getMissed ()
        {
            return missed;
        }

        public void setMissed (String missed)
        {
            this.missed = missed;
        }

        public User getUser ()
        {
            return user;
        }

        public void setUser (User user)
        {
            this.user = user;
        }*/

        /*       public null getAggregationSessionId ()
           {
               return aggregationSessionId;
           }

               public void setAggregationSessionId (null aggregationSessionId)
               {
                   this.aggregationSessionId = aggregationSessionId;
               }
       */
        @Override
        public String toString()
        {
            return  "";
            // return "ClassPojo [sessionCost = "+sessionCost+", connected = "+connected+", endTime = "+endTime+", id = "+id+", startTime = "+startTime+", declined = "+declined+", createdAt = "+createdAt+", userId = "+userId+", afterSessionCustomerEmotionId = "+afterSessionCustomerEmotionId+", customerRating = "+customerRating+", reasonForTheCall = "+reasonForTheCall+", sessionRatings = "+sessionRatings+", creditCardCharges = "+creditCardCharges+", sessionType = "+sessionType+", updatedAt = "+updatedAt+", pacifyrId = "+pacifyrId+", afterSessionPacifyrEmotionId = "+afterSessionPacifyrEmotionId+", duration = "+duration+", pacifyrEarnings = "+pacifyrEarnings+", stopped = "+stopped+", anonymousCall = "+anonymousCall+", pacifyr = "+pacifyr+", scheduledSessionId = "+scheduledSessionId+", missed = "+missed+", user = "+user+", aggregationSessionId = "+aggregationSessionId+"]";
        }
    }



    public class User
    {
        private String dateOfBirth;

        private String qualifications;

        private String[] employedToCompany;

        private String state;

        private String qbid;

        private String maritalStatus;

        private String[] education;

        private String loginType;

        private String online;

        private String id;

        private String availableMinutes;

        private String phoneNumber;

        private String specialities;

        private Double walletBalance;

        private String createdAt;

        private String description;

        private String gender;

        private String firstName;

        private String bonusProfileId;

        private String roleId;

        private String selectedSpeciality;

        private String lastName;

        private String emotionId;

        private String websiteURL;

        private String image;

        private String termsAccepted;

        private String currentlyAvailable;

        private MSessionRoot.RoleModel roleModel;

        private String emotion;

        private String country;

        private String updatedAt;

        private String[] certification;

        private String price;

        private String numberOfReviews;

        private String pin;

        private String email;

        private String companyId;

        private String rating;

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

        public String getQbid ()
        {
            return qbid;
        }

        public void setQbid (String qbid)
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

        public String getSpecialities ()
        {
            return specialities;
        }

        public void setSpecialities (String specialities)
        {
            this.specialities = specialities;
        }

        public Double getWalletBalance ()
        {
            return walletBalance;
        }

        public void setWalletBalance (Double walletBalance)
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

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getTermsAccepted ()
        {
            return termsAccepted;
        }

        public void setTermsAccepted (String termsAccepted)
        {
            this.termsAccepted = termsAccepted;
        }

        public String getCurrentlyAvailable ()
        {
            return currentlyAvailable;
        }

        public void setCurrentlyAvailable (String currentlyAvailable)
        {
            this.currentlyAvailable = currentlyAvailable;
        }

        public MSessionRoot.RoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (MSessionRoot.RoleModel roleModel)
        {
            this.roleModel = roleModel;
        }

        public String getEmotion ()
        {
            return emotion;
        }

        public void setEmotion (String emotion)
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
    }
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
}