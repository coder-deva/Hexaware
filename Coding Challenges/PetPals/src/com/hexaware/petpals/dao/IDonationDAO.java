package com.hexaware.petpals.dao;

import com.hexaware.petpals.entity.Donation;
import java.util.List;

public interface IDonationDAO {
    void recordDonation(Donation donation);
    List<Donation> getAllDonations();
    List<Donation> viewDonations(); 
}
