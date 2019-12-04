package com.pacifyrapp.utilitylibrary.model.model.pacifyr;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class MListEmployees
{
    private String updatedAt;

    private String id;

    private String employeeId;

    private String lastName;

    private String phoneNumber;

    private String email;

    private String createdAt;

    private String name;

    private String numberOfSessions;

    private String companyId;

    private String firstName;

    private String alreadyRegistered;

    private String roleId;

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

    public String getEmployeeId ()
    {
        return employeeId;
    }

    public void setEmployeeId (String employeeId)
    {
        this.employeeId = employeeId;
    }

    public String getLastName ()
    {
        return lastName;
    }

    public void setLastName (String lastName)
    {
        this.lastName = lastName;
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

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public String getNumberOfSessions ()
    {
        return numberOfSessions;
    }

    public void setNumberOfSessions (String numberOfSessions)
    {
        this.numberOfSessions = numberOfSessions;
    }

    public String getCompanyId ()
    {
        return companyId;
    }

    public void setCompanyId (String companyId)
    {
        this.companyId = companyId;
    }

    public String getFirstName ()
    {
        return firstName;
    }

    public void setFirstName (String firstName)
    {
        this.firstName = firstName;
    }

    public String getAlreadyRegistered ()
    {
        return alreadyRegistered;
    }

    public void setAlreadyRegistered (String alreadyRegistered)
    {
        this.alreadyRegistered = alreadyRegistered;
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
        return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", employeeId = "+employeeId+", lastName = "+lastName+", phoneNumber = "+phoneNumber+", email = "+email+", createdAt = "+createdAt+", name = "+name+", numberOfSessions = "+numberOfSessions+", companyId = "+companyId+", firstName = "+firstName+", alreadyRegistered = "+alreadyRegistered+", roleId = "+roleId+"]";
    }
}