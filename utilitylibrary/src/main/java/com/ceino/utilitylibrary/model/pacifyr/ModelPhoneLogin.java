package com.ceino.utilitylibrary.model.pacifyr;

/**
 * Created by Samsad on 13/7/17.
 */

public class ModelPhoneLogin {

    private String accessToken;

    private String status;

    private MUser user;

    private RoleModel roleModel;

    private String message;

    public String getAccessToken ()
    {
        return accessToken;
    }

    public void setAccessToken (String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public MUser getUser ()
    {
        return user;
    }

    public void setUser (MUser user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [accessToken = "+accessToken+", status = "+status+", user = "+user+"]";
    }

    public String getMessage() {
        return message;
    }

    public RoleModel getRoleModel()
    {
        return roleModel;
    }

    public void setRoleModel(RoleModel roleModel)
    {
        this.roleModel=roleModel;
    }


   /* public class User
    {
        private String lastName;

        private String qualifications;

        private String image;

        private String qbid;

        private String[] education;

        private RoleModel roleModel;

        private String loginType;

        private String online;

        private String updatedAt;

        private String id;

        private String[] certification;

        private String price;

        private String[] specialities;

        private String phoneNumber;

        private String email;

        private String pin;

        private String numberOfReviews;

        private String description;

        private String createdAt;

        private String gender;

        private String rating;

        private String firstName;

        private String roleId;

        public String getLastName ()
        {
            return lastName;
        }

        public void setLastName (String lastName)
        {
            this.lastName = lastName;
        }

        public String getQualifications ()
    {
        return qualifications;
    }

        public void setQualifications (String qualifications)
        {
            this.qualifications = qualifications;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
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

        public RoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (RoleModel roleModel)
        {
            this.roleModel = roleModel;
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

        public String[] getSpecialities ()
        {
            return specialities;
        }

        public void setSpecialities (String[] specialities)
        {
            this.specialities = specialities;
        }

        public String getPhoneNumber ()
        {
            return phoneNumber;
        }

        public void setPhoneNumber (String phoneNumber)
        {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
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

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public String getGender ()
    {
        return gender;
    }

        public void setGender (String gender)
        {
            this.gender = gender;
        }

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
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

        @Override
        public String toString()
        {
            return "ClassPojo [lastName = "+lastName+", qualifications = "+qualifications+", image = "+image+", qbid = "+qbid+", education = "+education+", roleModel = "+roleModel+", loginType = "+loginType+", online = "+online+", updatedAt = "+updatedAt+", id = "+id+", certification = "+certification+", price = "+price+", specialities = "+specialities+", phoneNumber = "+phoneNumber+", email = "+email+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", description = "+description+", createdAt = "+createdAt+", gender = "+gender+", rating = "+rating+", firstName = "+firstName+", roleId = "+roleId+"]";
        }
    }*/



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
