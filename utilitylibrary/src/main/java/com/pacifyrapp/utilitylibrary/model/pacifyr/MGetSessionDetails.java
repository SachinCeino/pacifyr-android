package com.pacifyrapp.utilitylibrary.model.pacifyr;

/**

 */

public class MGetSessionDetails
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

    private Object specialities;

    private String createdAt;

    private String description;

    private String gender;

    private String firstName;

    private String roleId;

    private String lastName;

    private String emotionId;

    private String websiteURL;

    private String image;

    private RoleModel roleModel;

    private Object emotion;

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

    public Object getSpecialities ()
    {
        return specialities;
    }

    public void setSpecialities (Object specialities)
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

    public RoleModel getRoleModel ()
    {
        return roleModel;
    }

    public void setRoleModel (RoleModel roleModel)
    {
        this.roleModel = roleModel;
    }

    public Object getEmotion ()
    {
        return emotion;
    }

    public void setEmotion (Object emotion)
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
        return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", walletBalance = "+walletBalance+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", companyId = "+companyId+", rating = "+rating+"]";
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
}

