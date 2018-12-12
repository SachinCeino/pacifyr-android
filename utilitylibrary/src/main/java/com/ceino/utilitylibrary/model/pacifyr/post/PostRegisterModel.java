package com.ceino.utilitylibrary.model.pacifyr.post;

/**
 * Created by ceino on 27/6/17.
 */

public class PostRegisterModel {

    private String country;

    private String image;

    private String dateOfBirth;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    private String lastName;

    private String email;

    private String deviceType="android";

    private String firstName;

    private String password;

    private String loginType;

    private String phoneNumber;

    private String description;

    private String selectedSpecialityCategory;

    private String emotionId;

    public PostRegisterModel() {

    }

    public PostRegisterModel(String emotionId, String selectedSpeciality) {
        this.selectedSpecialityCategory = selectedSpeciality;
        this.emotionId = emotionId;
    }

    public PostRegisterModel(String emotionId) {
        this.emotionId = emotionId;
    }
    public String getSelectedSpeciality() {
        return selectedSpecialityCategory;
    }

    public void setSelectedSpeciality(String selectedSpeciality) {
        this.selectedSpecialityCategory = selectedSpeciality;
    }

    public String getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(String emotionId) {
        this.emotionId = emotionId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
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

    @Override
    public String toString()
    {
        return "ClassPojo [lastName = "+lastName+", email = "+email+", firstName = "+firstName+", password = "+password+", loginType = "+loginType+"]";
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
