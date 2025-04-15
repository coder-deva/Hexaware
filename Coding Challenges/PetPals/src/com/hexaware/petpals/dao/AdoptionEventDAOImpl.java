package com.hexaware.petpals.dao;

import com.hexaware.petpals.entity.Pet;  

import java.util.ArrayList;
import java.util.List;


public class AdoptionEventDAOImpl implements IAdoptionEventDAO {

    
    private List<Pet> participants;

    
    public AdoptionEventDAOImpl() {
        this.participants = new ArrayList<>();
    }

    
    public void registerParticipant(Pet pet) {
        if (pet == null) {
            System.out.println("Cannot register a null participant.");
        } else {
            participants.add(pet);
            System.out.println(pet.getName() + " has been successfully registered.");
        }
    }

    
    @Override
    public void hostAdoptionEvent() {
        if (participants.isEmpty()) {
            System.out.println("No participants in the event.");
            return;
        }

        System.out.println("Hosting Adoption Event...");
        for (Pet pet : participants) {
            pet.adopt(); 
        }
    }
}
