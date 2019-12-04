package com.pacifyrapp.utilitylibrary.model.pacifyr;

/**
 * Created by Samsad on 11/August/2017.
 * samsad@ceinotech.com
 */

public class MGetSavedCards
{
    private String updatedAt;

    private String id;

    private String cardExpiryYear;

    private String cardName;

    private String createdAt;

    private String userId;

    private String cardLast4;

    private String cardExpiryMonth;

    private String cardBrand;

    private String stripeCustomerId;

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

    public String getCardExpiryYear ()
    {
        return cardExpiryYear;
    }

    public void setCardExpiryYear (String cardExpiryYear)
    {
        this.cardExpiryYear = cardExpiryYear;
    }

    public String getCardName ()
    {
        return cardName;
    }

    public void setCardName (String cardName)
    {
        this.cardName = cardName;
    }

    public String getCreatedAt ()
    {
        return createdAt;
    }

    public void setCreatedAt (String createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getUserId ()
    {
        return userId;
    }

    public void setUserId (String userId)
    {
        this.userId = userId;
    }

    public String getCardLast4 ()
    {
        return cardLast4;
    }

    public void setCardLast4 (String cardLast4)
    {
        this.cardLast4 = cardLast4;
    }

    public String getCardExpiryMonth ()
    {
        return cardExpiryMonth;
    }

    public void setCardExpiryMonth (String cardExpiryMonth)
    {
        this.cardExpiryMonth = cardExpiryMonth;
    }

    public String getCardBrand ()
    {
        return cardBrand;
    }

    public void setCardBrand (String cardBrand)
    {
        this.cardBrand = cardBrand;
    }

    public String getStripeCustomerId ()
    {
        return stripeCustomerId;
    }

    public void setStripeCustomerId (String stripeCustomerId)
    {
        this.stripeCustomerId = stripeCustomerId;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [updatedAt = "+updatedAt+", id = "+id+", cardExpiryYear = "+cardExpiryYear+", cardName = "+cardName+", createdAt = "+createdAt+", userId = "+userId+", cardLast4 = "+cardLast4+", cardExpiryMonth = "+cardExpiryMonth+", cardBrand = "+cardBrand+", stripeCustomerId = "+stripeCustomerId+"]";
    }
}