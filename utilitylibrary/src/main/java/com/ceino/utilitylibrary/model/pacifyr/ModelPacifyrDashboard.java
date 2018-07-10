package com.ceino.utilitylibrary.model.pacifyr;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Samsad on 25/July/2017.
 * samsad@ceinotech.com
 */

public class ModelPacifyrDashboard
{
    private Object upcomingSession;

    private MLastSession lastSession;

    private ModelStatistics todayStatistics;

    private Rating rating;

    private MUser[] customers;

    private MUser user;

    public void setCutomers (ArrayList<MUser> list)
    {

        if(list!=null)
            this.customers=list.toArray( (new MUser[list.size()]));
    }

    public ArrayList<MUser> getCustomerList() {
        ArrayList<MUser> arrayList = null;
        try {
            if(customers != null) {
                arrayList = new ArrayList<MUser>(Arrays.asList(customers));

            }
        } catch (Exception e){//Crashlytics.logException(e);
            arrayList = null;
        }

        return arrayList;
    }
    public void addCustomers (MUser[] results)
    {
        ArrayList<MUser> arrayList = new ArrayList<MUser>();
        if(getCustomerList()!=null)
            arrayList.addAll(getCustomerList());
        if(results!=null)
            arrayList.addAll(Arrays.asList(results));
        if(arrayList!=null)
            this.customers=arrayList.toArray( (new MUser[arrayList.size()]));


    }


    public Object getUpcomingSession ()
    {
        return upcomingSession;
    }

    public void setUpcomingSession (Object upcomingSession)
    {
        this.upcomingSession = upcomingSession;
    }

    public MLastSession getLastSession ()
    {
        return lastSession;
    }

    public void setLastSession (MLastSession lastSession)
    {
        this.lastSession = lastSession;
    }

    public ModelStatistics getTodayStatistics ()
    {
        return todayStatistics;
    }

    public void setTodayStatistics (ModelStatistics todayStatistics)
    {
        this.todayStatistics = todayStatistics;
    }

    public Rating getRating ()
    {
        return rating;
    }

    public void setRating (Rating rating)
    {
        this.rating = rating;
    }

    public MUser[] getCustomers ()
    {
        return customers;
    }

    public void setCustomers (MUser[] customers)
    {
        this.customers = customers;
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
        return "ClassPojo [upcomingSession = "+upcomingSession+", lastSession = "+lastSession+", todayStatistics = "+todayStatistics+", rating = "+rating+", customers = "+customers+", user = "+user+"]";
    }


    public class TodayStatistics
    {
        private String earnings;

        private String totalSessions;

        private String duration;

        private String availableMins;

        public String getEarnings ()
        {
            return earnings;
        }

        public void setEarnings (String earnings)
        {
            this.earnings = earnings;
        }

        public String getTotalSessions ()
        {
            return totalSessions;
        }

        public void setTotalSessions (String totalSessions)
        {
            this.totalSessions = totalSessions;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getAvailableMins ()
        {
            return availableMins;
        }

        public void setAvailableMins (String availableMins)
        {
            this.availableMins = availableMins;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [earnings = "+earnings+", totalSessions = "+totalSessions+", duration = "+duration+", availableMins = "+availableMins+"]";
        }
    }




    /*public class LastSession
    {
        private String sessionCost;

        private String sessionType;

        private String endTime;

        private String updatedAt;

        private String pacifyrId;

        private String startTime;

        private String id;

        private String duration;

        private String stopped;

        private String createdAt;

        private Object pacifyr;

        private String userId;

        private User user;

        private String aggregationSessionId;

        private String customerRating;

        public String getSessionCost ()
        {
            return sessionCost;
        }

        public void setSessionCost (String sessionCost)
        {
            this.sessionCost = sessionCost;
        }

        public String getSessionType ()
        {
            return sessionType;
        }

        public void setSessionType (String sessionType)
        {
            this.sessionType = sessionType;
        }

        public String getEndTime ()
        {
            return endTime;
        }

        public void setEndTime (String endTime)
        {
            this.endTime = endTime;
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

        public String getStartTime ()
        {
            return startTime;
        }

        public void setStartTime (String startTime)
        {
            this.startTime = startTime;
        }

        public String getId ()
        {
            return id;
        }

        public void setId (String id)
        {
            this.id = id;
        }

        public String getDuration ()
        {
            return duration;
        }

        public void setDuration (String duration)
        {
            this.duration = duration;
        }

        public String getStopped ()
        {
            return stopped;
        }

        public void setStopped (String stopped)
        {
            this.stopped = stopped;
        }

        public String getCreatedAt ()
        {
            return createdAt;
        }

        public void setCreatedAt (String createdAt)
        {
            this.createdAt = createdAt;
        }

        public Object getPacifyr ()
        {
             return pacifyr;
         }

        public void setPacifyr (Object pacifyr)
        {
            this.pacifyr = pacifyr;
        }

        public String getUserId ()
        {
            return userId;
        }

        public void setUserId (String userId)
        {
            this.userId = userId;
        }

        public User getUser ()
        {
            return user;
        }

        public void setUser (User user)
        {
            this.user = user;
        }

        public String getAggregationSessionId ()
        {
            return aggregationSessionId;
        }

        public void setAggregationSessionId (String aggregationSessionId)
        {
            this.aggregationSessionId = aggregationSessionId;
        }

        public String getCustomerRating ()
        {
            return customerRating;
        }

        public void setCustomerRating (String customerRating)
        {
            this.customerRating = customerRating;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [sessionCost = "+sessionCost+", sessionType = "+sessionType+", endTime = "+endTime+", updatedAt = "+updatedAt+", pacifyrId = "+pacifyrId+", startTime = "+startTime+", id = "+id+", duration = "+duration+", stopped = "+stopped+", createdAt = "+createdAt+", pacifyr = "+pacifyr+", userId = "+userId+", user = "+user+", aggregationSessionId = "+aggregationSessionId+", customerRating = "+customerRating+"]";
        }
    }
*/


    public class User
    {
        private String qualifications;

        private String qbid;

        private String[] education;

        private String loginType;

        private String online;

        private String id;

        private String availableMinutes;

        private String phoneNumber;

        private String[] specialities;

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

        private MEmotion emotion;

        private String updatedAt;

        private String[] certification;

        private String price;

        private String pin;

        private String numberOfReviews;

        private String email;

        private String rating;

        private String companyId;

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

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        public String getCompanyId ()
    {
        return companyId;
    }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", rating = "+rating+", companyId = "+companyId+"]";
        }
    }



    public class Customers
    {
        private String qualifications;

        private String qbid;

        private String[] education;

        private String loginType;

        private String online;

        private String id;

        private String availableMinutes;

        private String phoneNumber;

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

        private String rating;

        private String companyId;

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

        public String  getLoginType ()
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

        public String getRating ()
        {
            return rating;
        }

        public void setRating (String rating)
        {
            this.rating = rating;
        }

        public String getCompanyId ()
        {
            return companyId;
        }

        public void setCompanyId (String companyId)
        {
            this.companyId = companyId;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [qualifications = "+qualifications+", qbid = "+qbid+", education = "+education+", loginType = "+loginType+", online = "+online+", id = "+id+", availableMinutes = "+availableMinutes+", phoneNumber = "+phoneNumber+", specialities = "+specialities+", createdAt = "+createdAt+", description = "+description+", gender = "+gender+", firstName = "+firstName+", roleId = "+roleId+", lastName = "+lastName+", emotionId = "+emotionId+", websiteURL = "+websiteURL+", image = "+image+", roleModel = "+roleModel+", emotion = "+emotion+", updatedAt = "+updatedAt+", certification = "+certification+", price = "+price+", pin = "+pin+", numberOfReviews = "+numberOfReviews+", email = "+email+", rating = "+rating+", companyId = "+companyId+"]";
        }
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






    public class Rating
    {
        private String fiveStarRatingCount;

        private String numberOfSessions;

        private String rating;

        public String getFiveStarRatingCount ()
        {
            return fiveStarRatingCount;
        }

        public void setFiveStarRatingCount (String fiveStarRatingCount)
        {
            this.fiveStarRatingCount = fiveStarRatingCount;
        }

        public String getNumberOfSessions ()
        {
            return numberOfSessions;
        }

        public void setNumberOfSessions (String numberOfSessions)
        {
            this.numberOfSessions = numberOfSessions;
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
            return "ClassPojo [fiveStarRatingCount = "+fiveStarRatingCount+", numberOfSessions = "+numberOfSessions+", rating = "+rating+"]";
        }
    }
}