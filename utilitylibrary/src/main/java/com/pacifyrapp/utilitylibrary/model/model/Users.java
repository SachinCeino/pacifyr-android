package com.pacifyrapp.utilitylibrary.model.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Users {
    private Results[] results;

    private int total_count;

    public Results[] getResults() {
        return results;
    }
    public ArrayList<Results> getResultsList() {
        ArrayList<Results> arrayList = null;
        try {
            if(results != null) {
                arrayList = new ArrayList<Results>(Arrays.asList(results));

            }
        } catch (Exception e){
            // Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addResults (Results[] results)
    {
        ArrayList<Results> arrayList =  new ArrayList<Results>();
        if(getResultsList()!=null)
            arrayList.addAll(getResultsList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.results=arrayList.toArray( (new Results[arrayList.size()]));


    }
    public void setResults(Results[] results) {
        this.results = results;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    @Override
    public String toString() {
        return "ClassPojo [results = " + results + ", total_count = " + total_count + "]";
    }

    public class Results
    {
        private String[] schoolid;

        private String lname;

        private String phone;

        private String userimageurl;

        private String[] accessToClass;

        private String isPublic;

        private String facebookUrl;

        private String updatedAt;

        private String _id;

        private String email;

        private String address;

        private String deviceType;

        private String linkedinUrl;

        private String createdAt;

        private String role;

        private String notes;

        private String fname;
        public boolean isSelected() {
            return isSelected;
        }

        private boolean isSelected=false;

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        private String image;


        public String[] getSchoolid ()
        {
            return schoolid;
        }

        public void setSchoolid (String[] schoolid)
        {
            this.schoolid = schoolid;
        }

        public String getLname ()
        {
            return lname;
        }

        public void setLname (String lname)
        {
            this.lname = lname;
        }

        public String getPhone ()
        {
            return phone;
        }

        public void setPhone (String phone)
        {
            this.phone = phone;
        }

        public String getUserimageurl ()
        {
            return userimageurl;
        }

        public void setUserimageurl (String userimageurl)
        {
            this.userimageurl = userimageurl;
        }

        public String[] getAccessToClass ()
        {
            return accessToClass;
        }

        public void setAccessToClass (String[] accessToClass)
        {
            this.accessToClass = accessToClass;
        }

        public String getIsPublic ()
        {
            return isPublic;
        }

        public void setIsPublic (String isPublic)
        {
            this.isPublic = isPublic;
        }

        public String getFacebookUrl ()
        {
            return facebookUrl;
        }

        public void setFacebookUrl (String facebookUrl)
        {
            this.facebookUrl = facebookUrl;
        }

        public String getUpdatedAt ()
        {
            return updatedAt;
        }

        public void setUpdatedAt (String updatedAt)
        {
            this.updatedAt = updatedAt;
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

        public String getRole ()
        {
            return role;
        }

        public void setRole (String role)
        {
            this.role = role;
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
            return "ClassPojo [schoolid = "+schoolid+", lname = "+lname+", phone = "+phone+", userimageurl = "+userimageurl+", accessToClass = "+accessToClass+", isPublic = "+isPublic+", facebookUrl = "+facebookUrl+", updatedAt = "+updatedAt+", _id = "+_id+", email = "+email+", address = "+address+", deviceType = "+deviceType+", linkedinUrl = "+linkedinUrl+", createdAt = "+createdAt+", role = "+role+", notes = "+notes+", fname = "+fname+"]";
        }
    }
}