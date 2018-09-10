package com.ceino.utilitylibrary.model.model.pacifyr;

import com.ceino.utilitylibrary.model.pacifyr.*;

/**
 * Created by ceino on 28/6/17.
 */

public class LoginModel
{
    private String accessToken;

    private String status;

    private com.ceino.utilitylibrary.model.pacifyr.MUser user;

    private String message;

    public void setMessage(String message) {
        this.message = message;
    }

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

    public com.ceino.utilitylibrary.model.pacifyr.MUser getUser ()
    {
        return user;
    }

    public void setUser (com.ceino.utilitylibrary.model.pacifyr.MUser user)
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


   /* public class User
    {
        private String lastName;

        private String location;

        private String currentCompany;

        private String image;

        private String qbid;

        private String linkedInProfileUrl;

        private String password;

        private String loginType;

        private RoleModel roleModel;

        private String updatedAt;

        private String id;

        private String email;

        private String currentJobTitle;

        private String createdAt;

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

        public String getLocation ()
    {
        return location;
    }

        public void setLocation (String location)
        {
            this.location = location;
        }

        public String getCurrentCompany ()
    {
        return currentCompany;
    }

        public void setCurrentCompany (String currentCompany)
        {
            this.currentCompany = currentCompany;
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

        public String getLinkedInProfileUrl ()
    {
        return linkedInProfileUrl;
    }

        public void setLinkedInProfileUrl (String linkedInProfileUrl)
        {
            this.linkedInProfileUrl = linkedInProfileUrl;
        }

        public String getPassword ()
        {
            return password;
        }

        public void setPassword (String password)
        {
            this.password = password;
        }

        public String getLoginType ()
        {
            return loginType;
        }

        public void setLoginType (String loginType)
        {
            this.loginType = loginType;
        }

        public RoleModel getRoleModel ()
        {
            return roleModel;
        }

        public void setRoleModel (RoleModel roleModel)
        {
            this.roleModel = roleModel;
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

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getCurrentJobTitle ()
    {
        return currentJobTitle;
    }

        public void setCurrentJobTitle (String currentJobTitle)
        {
            this.currentJobTitle = currentJobTitle;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
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
            return "ClassPojo [lastName = "+lastName+", location = "+location+", currentCompany = "+currentCompany+", image = "+image+", qbid = "+qbid+", linkedInProfileUrl = "+linkedInProfileUrl+", password = "+password+", loginType = "+loginType+", roleModel = "+roleModel+", updatedAt = "+updatedAt+", id = "+id+", email = "+email+", currentJobTitle = "+currentJobTitle+", createdAt = "+createdAt+", firstName = "+firstName+", roleId = "+roleId+"]";
        }
    }*/



    /*public class RoleModel
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
    }*/
}