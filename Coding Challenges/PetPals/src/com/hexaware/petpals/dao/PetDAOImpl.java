package com.hexaware.petpals.dao;

import com.hexaware.petpals.entity.Pet;
import com.hexaware.petpals.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetDAOImpl implements IPetDAO {

    @Override
    public void addPet(Pet pet) {
  
    	String query = "INSERT INTO pets (name, age, breed, type, availableForAdoption) VALUES (?, ?, ?, ?, ?)";

    	try (Connection connection = DBUtil.getConnection();
    	     PreparedStatement preparedStatement = connection.prepareStatement(query)) {

    	    preparedStatement.setString(1, pet.getName());
    	    preparedStatement.setInt(2, pet.getAge());
    	    preparedStatement.setString(3, pet.getBreed());
    	    preparedStatement.setString(4, pet.getType());
    	    preparedStatement.setBoolean(5, true); 

    	    preparedStatement.executeUpdate();
    	    System.out.println("Pet added successfully!");


        } catch (SQLException e) {
            System.out.println("Error adding pet: " + e.getMessage());
        }
    }

    @Override
    public List<Pet> getAllPets() {
        List<Pet> petList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String query = "SELECT * FROM pets";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Pet pet = new Pet(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("breed")
                );
                petList.add(pet);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving pets: " + e.getMessage());
        }
        return petList;
    }

    @Override
    public void removePetByName(String name) {
        try (Connection conn = DBUtil.getConnection()) {
            String query = "DELETE FROM pets WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Pet removed successfully.");
            } else {
                System.out.println("Pet not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error removing pet: " + e.getMessage());
        }
    }

    @Override
    public void listPets() {
        List<Pet> pets = getAllPets();
        if (pets.isEmpty()) {
            System.out.println("No pets available.");
        } else {
            System.out.println("Available Pets:");
            for (Pet pet : pets) {
                System.out.println("Name: " + pet.getName() + ", Age: " + pet.getAge() + ", Breed: " + pet.getBreed());
            }
        }
    }
}
