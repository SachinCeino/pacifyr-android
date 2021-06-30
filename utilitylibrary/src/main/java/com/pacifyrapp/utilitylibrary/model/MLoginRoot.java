package com.pacifyrapp.utilitylibrary.model;

import com.pacifyrapp.utilitylibrary.network.UtilityNetworkService;

/**

 */

public class MLoginRoot {
    private String token;

    private String status;
    private String message;
    protected User user;
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getToken ()
    {
        return token;
    }

    public void setToken (String token)
    {
        this.token = token;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public User getUser ()
    {
        return user;
    }

    public void setUser (User user)
    {
        this.user = user;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [token = "+token+", status = "+status+", user = "+user+"]";
    }
    public class User
    {
        private String[] schoolid;

        private String phone;

        private String lname;

        private String image;

        private String couponCode;

        private String isRejected;

        private String isPublic;

        private String[] accessToClass;

        private String code;

        private String facebookUrl;

        private String password;

        private String updatedAt;

        private String fromTopschool;

        private String thumbnail;

        private String _id;

        private String email;

        private String address;

        private String deviceType;

        private String linkedinUrl;

        private String createdAt;

        private Role role;

        private String isemailverified;

        private String notes;

        private String fname;

        private String userimageurl;

        private String[] grade;

        public String[] getGrade ()
        {
            return grade;
        }

        public void setGrade (String[] grade)
        {
            this.grade = grade;
        }




        public String getProfileImgUrl()
        {
            String url=null;
            try{
                if(isValidhtml(userimageurl))
                    url=removeExtraSlashes(userimageurl);
                else if(isValid(thumbnail))
                    url= UtilityNetworkService.GLOBAL_IMAGE_URL+thumbnail;
                else if(isValid(image))
                    url=UtilityNetworkService.GLOBAL_IMAGE_URL+image;


            }catch (Exception e)
            {

            }
            return url;//
        }

        public String removeExtraSlashes(String text) {
            if(isValid(text)) if(text.contains("\\\\")) return text.replaceAll("\\\\","");
            return text;

        }
        public Boolean isValid(String text) {
            if(text != null) if(!text.trim().equalsIgnoreCase("")) return true;
            return false;

        }

        public Boolean isValidhtml(String text) {
            if(isValid(text)) if(text.contains("http")) return true;
            return false;

        }
        public String[] getSchoolid ()
        {
            return schoolid;
        }

        public void setSchoolid (String[] schoolid)
        {
            this.schoolid = schoolid;
        }

        public String getPhone ()
        {
            return phone;
        }

        public void setPhone (String phone)
        {
            this.phone = phone;
        }

        public String getLname ()
        {
            return lname;
        }

        public void setLname (String lname)
        {
            this.lname = lname;
        }

        public String getImage ()
        {
            return image;
        }

        public void setImage (String image)
        {
            this.image = image;
        }

        public String getCouponCode ()
        {
            return couponCode;
        }

        public void setCouponCode (String couponCode)
        {
            this.couponCode = couponCode;
        }

        public String getIsRejected ()
        {
            return isRejected;
        }

        public void setIsRejected (String isRejected)
        {
            this.isRejected = isRejected;
        }

        public String getIsPublic ()
        {
            return isPublic;
        }

        public void setIsPublic (String isPublic)
        {
            this.isPublic = isPublic;
        }

        public String[] getAccessToClass ()
        {
            return accessToClass;
        }

        public void setAccessToClass (String[] accessToClass)
        {
            this.accessToClass = accessToClass;
        }

        public String getCode ()
        {
            return code;
        }

        public void setCode (String code)
        {
            this.code = code;
        }

        public String getFacebookUrl ()
        {
            return facebookUrl;
        }

        public void setFacebookUrl (String facebookUrl)
        {
            this.facebookUrl = facebookUrl;
        }

        public String getPassword ()
        {
            return password;
        }

        public void setPassword (String password)
        {
            this.password = password;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
        }

        public String getFromTopschool ()
        {
            return fromTopschool;
        }

        public void setFromTopschool (String fromTopschool)
        {
            this.fromTopschool = fromTopschool;
        }

        public String getThumbnail ()
        {
            return thumbnail;
        }

        public void setThumbnail (String thumbnail)
        {
            this.thumbnail = thumbnail;
        }

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String getEmail ()
        {
            return email;
        }

        public void setEmail (String email)
        {
            this.email = email;
        }

        public String getAddress ()
        {
            return address;
        }

        public void setAddress (String address)
        {
            this.address = address;
        }

        public String getDeviceType ()
        {
            return deviceType;
        }

        public void setDeviceType (String deviceType)
        {
            this.deviceType = deviceType;
        }

        public String getLinkedinUrl ()
        {
            return linkedinUrl;
        }

        public void setLinkedinUrl (String linkedinUrl)
        {
            this.linkedinUrl = linkedinUrl;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public Role getRole ()
        {
            return role;
        }

        public void setRole (Role role)
        {
            this.role = role;
        }

        public String getIsemailverified ()
        {
            return isemailverified;
        }

        public void setIsemailverified (String isemailverified)
        {
            this.isemailverified = isemailverified;
        }

        public String getNotes ()
        {
            return notes;
        }

        public void setNotes (String notes)
        {
            this.notes = notes;
        }

        public String getFname ()
        {
            return fname;
        }

        public void setFname (String fname)
        {
            this.fname = fname;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [schoolid = "+schoolid+", phone = "+phone+", lname = "+lname+", image = "+image+", couponCode = "+couponCode+", isRejected = "+isRejected+", isPublic = "+isPublic+", accessToClass = "+accessToClass+", code = "+code+", facebookUrl = "+facebookUrl+", password = "+password+", updatedAt = "+updatedAt+", fromTopschool = "+fromTopschool+", thumbnail = "+thumbnail+", _id = "+_id+", email = "+email+", address = "+address+", deviceType = "+deviceType+", linkedinUrl = "+linkedinUrl+", createdAt = "+createdAt+", role = "+role+", isemailverified = "+isemailverified+", notes = "+notes+", fname = "+fname+"]";
        }


    }
    public class Role
    {
        private String _id;

        private String[] permissions;

        private String role;

        public String get_id ()
        {
            return _id;
        }

        public void set_id (String _id)
        {
            this._id = _id;
        }

        public String[] getPermissions ()
        {
            return permissions;
        }

        public void setPermissions (String[] permissions)
        {
            this.permissions = permissions;
        }

        public String getRole ()
        {
            return role;
        }

        public void setRole (String role)
        {
            this.role = role;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [_id = "+_id+", permissions = "+permissions+", role = "+role+"]";
        }
    }
/*
    private User user;

    public User getUser() { return this.user; }

    public void setUser(User user) { this.user = user; }

    private String status;

    public String getStatus() { return this.status; }

    public void setStatus(String status) { this.status = status; }

    private String token;

    public String getToken() { return this.token; }

    public void setToken(String token) { this.token = token; }



    public class Role
    {
        private String role;

        public String getRole() { return this.role; }

        public void setRole(String role) { this.role = role; }

        private ArrayList<String> permissions;

        public ArrayList<String> getPermissions() { return this.permissions; }

        public void setPermissions(ArrayList<String> permissions) { this.permissions = permissions; }

        private String _id;

        public String getId() { return this._id; }

        public void setId(String _id) { this._id = _id; }
    }

    public class User
    {
        private boolean isemailverified;

        public boolean getIsemailverified() { return this.isemailverified; }

        public void setIsemailverified(boolean isemailverified) { this.isemailverified = isemailverified; }

        private String fname;

        public String getFname() { return this.fname; }

        public void setFname(String fname) { this.fname = fname; }

        private String code;

        public String getCode() { return this.code; }

        public void setCode(String code) { this.code = code; }

        private Role role;

        public Role getRole() { return this.role; }

        public void setRole(Role role) { this.role = role; }

        private String loginType;

        public String getLoginType() { return this.loginType; }

        public void setLoginType(String loginType) { this.loginType = loginType; }

        private boolean fromTopschool;

        public boolean getFromTopschool() { return this.fromTopschool; }

        public void setFromTopschool(boolean fromTopschool) { this.fromTopschool = fromTopschool; }

        private boolean isrejected;

        public boolean getIsrejected() { return this.isrejected; }

        public void setIsrejected(boolean isrejected) { this.isrejected = isrejected; }

        private Date createdAt;

        public Date getCreatedAt() { return this.createdAt; }

        public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

        private String lname;

        public String getLname() { return this.lname; }

        public void setLname(String lname) { this.lname = lname; }

        private String password;

        public String getPassword() { return this.password; }

        public void setPassword(String password) { this.password = password; }

        private ArrayList<Object> classid;

        public ArrayList<Object> getClassid() { return this.classid; }

        public void setClassid(ArrayList<Object> classid) { this.classid = classid; }

        private String phone;

        public String getPhone() { return this.phone; }

        public void setPhone(String phone) { this.phone = phone; }

        private ArrayList<String> schoolid;

        public ArrayList<String> getSchoolid() { return this.schoolid; }

        public void setSchoolid(ArrayList<String> schoolid) { this.schoolid = schoolid; }

        private ArrayList<Object> accessToClass;

        public ArrayList<Object> getAccessToClass() { return this.accessToClass; }

        public void setAccessToClass(ArrayList<Object> accessToClass) { this.accessToClass = accessToClass; }

        private String _id;

        public String getId() { return this._id; }

        public void setId(String _id) { this._id = _id; }

        private String couponCode;

        public String getCouponCode() { return this.couponCode; }

        public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

        private String email;

        public String getEmail() { return this.email; }

        public void setEmail(String email) { this.email = email; }

        private Date updatedAt;

        public Date getUpdatedAt() { return this.updatedAt; }

        public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    }*/
}
