package com.hexaware.petpals.main;

import java.util.Scanner;
import com.hexaware.petpals.dao.IDonationDAO;
import com.hexaware.petpals.dao.DonationDAOImpl;
import com.hexaware.petpals.dao.IPetDAO;
import com.hexaware.petpals.dao.PetDAOImpl;
import com.hexaware.petpals.entity.CashDonation;
import com.hexaware.petpals.entity.Cat;
import com.hexaware.petpals.entity.Dog;
import com.hexaware.petpals.entity.ItemDonation;
import com.hexaware.petpals.entity.Pet;

public class MainModule {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        IPetDAO petDAO = new PetDAOImpl();
        IDonationDAO donationDAO = new DonationDAOImpl();

        int choice = 0;
        do {
            System.out.println("\n=== PetPals Adoption Platform ===");
            System.out.println("1. List Available Pets");
            System.out.println("2. Add Pet");
            System.out.println("3. Record Donation");
            System.out.println("4. View Donations");
            System.out.println("5. Host Adoption Event");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        petDAO.listPets();
                        break;

                    case 2:
                        addPet(scanner, petDAO);
                        break;

                    case 3:
                        recordDonation(scanner, donationDAO);
                        break;

                    case 4:
                        donationDAO.viewDonations();
                        break;

                    case 5:
                        hostAdoptionEvent();
                        break;

                    case 6:
                        System.out.println("Thank you for using PetPals!");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Something went wrong: " + e.getMessage());
            }

        } while (choice != 6);

        scanner.close();
    }

    // Method to add a pet
    private static void addPet(Scanner scanner, IPetDAO petDAO) {
        System.out.print("Enter Pet Type (dog/cat): ");
        String type = scanner.nextLine().toLowerCase();

        System.out.print("Enter Pet Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        if (age <= 0) throw new IllegalArgumentException("Invalid pet age");

        System.out.print("Enter Breed: ");
        String breed = scanner.nextLine();

        if (type.equals("dog")) {
            System.out.print("Enter Dog Breed: ");
            String dogBreed = scanner.nextLine();
            petDAO.addPet(new Dog(name, age, breed, dogBreed));
        } else if (type.equals("cat")) {
            System.out.print("Enter Cat Color: ");
            String color = scanner.nextLine();
            petDAO.addPet(new Cat(name, age, breed, color));
        } else {
            System.out.println("Invalid pet type!");
        }
    }

    // Method to record a donation
    private static void recordDonation(Scanner scanner, IDonationDAO donationDAO) {
        try {
            System.out.print("Enter Donor Name: ");
            String donorName = scanner.nextLine();

            System.out.print("Enter Donation Amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount < 10) throw new Exception("Insufficient Funds: Minimum $10 required.");

            System.out.print("Donation Type (cash/item): ");
            String donationType = scanner.nextLine().toLowerCase();

            if (donationType.equals("cash")) {
                CashDonation cashDonation = new CashDonation(donorName, amount, new java.util.Date());
                donationDAO.recordDonation(cashDonation);
            } else if (donationType.equals("item")) {
                System.out.print("Enter Item Type: ");
                String itemType = scanner.nextLine();
                ItemDonation itemDonation = new ItemDonation(donorName, amount, itemType);
                donationDAO.recordDonation(itemDonation);
            } else {
                System.out.println("Invalid donation type.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid amount entered. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to host the adoption event
    private static void hostAdoptionEvent() {
        System.out.println("Hosting adoption event...");
        System.out.println("Participants: Shelter, Donors, Visitors");
        System.out.println("Event Hosted Successfully!");
    }
}
