package com.hexaware.petpals.dao;

import com.hexaware.petpals.entity.*;
import com.hexaware.petpals.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DonationDAOImpl implements IDonationDAO {

    @Override
    public void recordDonation(Donation donation) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "INSERT INTO donations (donor_name, amount, type, extra_info) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, donation.getDonorName());
            pstmt.setDouble(2, donation.getAmount());

            if (donation instanceof CashDonation) {
                pstmt.setString(3, "Cash");
                pstmt.setString(4, ((CashDonation) donation).getDonationDate().toString());
            } else if (donation instanceof ItemDonation) {
                pstmt.setString(3, "Item");
                pstmt.setString(4, ((ItemDonation) donation).getItemType());
            }

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Donation> getAllDonations() {
        List<Donation> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM donations";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String type = rs.getString("type");
                String donor = rs.getString("donor_name");
                double amount = rs.getDouble("amount");
                String extra = rs.getString("extra_info");

                if ("Cash".equalsIgnoreCase(type)) {
                    list.add(new CashDonation(donor, amount, Date.valueOf(extra)));  // Parse extra as Date
                } else if ("Item".equalsIgnoreCase(type)) {
                    list.add(new ItemDonation(donor, amount, extra));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Donation> viewDonations() {
        return getAllDonations();  
    }
}
