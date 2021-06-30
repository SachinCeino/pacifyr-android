package com.pacifyrapp.utilitylibrary.model.model.pacifyr;

/**

 */

public class ChatSubscription
{
    private String updatedAt;

    private String pacifyrId;

    private String id;

    private String amount;

    private String createdAt;

    private String expiryDate;

    private String userId;

    private String purchaseDate;

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

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getExpiryDate ()
    {
        return expiryDate;
    }

    public void setExpiryDate (String expiryDate)
    {
        this.expiryDate = expiryDate;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getPurchaseDate ()
    {
        return purchaseDate;
    }

    public void setPurchaseDate (String purchaseDate)
    {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [updatedAt = "+updatedAt+", pacifyrId = "+pacifyrId+", id = "+id+", amount = "+amount+", createdAt = "+createdAt+", expiryDate = "+expiryDate+", userId = "+userId+", purchaseDate = "+purchaseDate+"]";
    }
}