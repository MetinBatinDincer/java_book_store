package com.bookstore.ui.model;

public class PaymentInfo {

    private String  cardHolder;
    private String  cardNumber;
    private String  expiry;
    private String  cvv;
    private boolean saveCard;

    public String  getCardHolder()           { return cardHolder; }
    public void    setCardHolder(String v)   { this.cardHolder = v; }

    public String  getCardNumber()           { return cardNumber; }
    public void    setCardNumber(String v)   { this.cardNumber = v; }

    public String  getExpiry()               { return expiry; }
    public void    setExpiry(String v)       { this.expiry = v; }

    public String  getCvv()                  { return cvv; }
    public void    setCvv(String v)          { this.cvv = v; }

    public boolean isSaveCard()              { return saveCard; }
    public void    setSaveCard(boolean v)    { this.saveCard = v; }
}
