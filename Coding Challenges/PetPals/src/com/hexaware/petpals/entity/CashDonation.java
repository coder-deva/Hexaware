package com.hexaware.petpals.entity;

import java.util.Date;

public class CashDonation extends Donation {
    private Date donationDate;

    
    public CashDonation(String donorName, double amount, Date donationDate) {
        super(donorName, amount);  
        this.donationDate = donationDate;
    }

    public Date getDonationDate() {
        return donationDate;
    }

    
    @Override
    public void recordDonation() {
        System.out.println("Cash Donation Recorded: " + donorName + " donated $" + amount + " on " + donationDate);
    }
}
