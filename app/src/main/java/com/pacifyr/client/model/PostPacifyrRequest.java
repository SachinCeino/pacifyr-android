package com.pacifyr.client.model;

import com.pacifyrapp.utilitylibrary.model.pacifyr.Education;
import com.pacifyrapp.utilitylibrary.model.pacifyr.ModelSpecialist;

import java.util.ArrayList;

/**

 */

public class PostPacifyrRequest {

    private String firstName;

    private String price;

    private String phoneNumber;

    private String lastName;

    private String country;

    private Education[] educations;

    private String description;

    private String email;

    private String[] specialities;

    private String dateOfBirth;

    private ModelPacifyrCertificates[] certifications;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String  phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Education[] getEducations() {
        return educations;
    }

    public void setEducations(Education[] educations) {
        this.educations = educations;
    }

    public void setEducation(ArrayList<Education> education) {

        this.educations = education.toArray(new Education[0]);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getSpecialities() {
        return specialities;
    }

    public void setSpecialities(String[] specialities) {
        this.specialities = specialities;
    }

    public void setSpecialities(ArrayList<ModelSpecialist.Results> specialities) {
        String[] sp = new String[specialities.size()];
        for (int i=0;i<specialities.size();i++){
            sp[i]=specialities.get(i).getTitle();
        }

        this.specialities = sp;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public ModelPacifyrCertificates[] getCertifications() {
        return certifications;
    }

    public void setCertifications(ModelPacifyrCertificates[] certifications) {
        this.certifications = certifications;
    }

    public void setCertifications(ArrayList<ModelPacifyrCertificates> certifications) {
        this.certifications = certifications.toArray(new ModelPacifyrCertificates[0]);
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
