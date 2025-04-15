package com.hexaware.petpals.dao;

import com.hexaware.petpals.entity.Pet;
import java.util.List;

public interface IPetDAO {

    
    void addPet(Pet pet);

    
    List<Pet> getAllPets();


    void removePetByName(String name);

    
    void listPets();
}
