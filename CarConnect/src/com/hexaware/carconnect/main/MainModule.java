package com.hexaware.carconnect.main;

import java.time.LocalDate;


import java.util.List;
import java.util.Scanner;

import com.hexaware.carconnect.util.EmailUtil;
import javax.mail.MessagingException;

import com.hexaware.carconnect.auth.AuthenticationService;
import com.hexaware.carconnect.entity.Admin;
import com.hexaware.carconnect.entity.Customer;
import com.hexaware.carconnect.entity.Vehicle;
import com.hexaware.carconnect.entity.Reservation;
import com.hexaware.carconnect.exception.AuthenticationException;
import com.hexaware.carconnect.exception.DatabaseConnectionException;
import com.hexaware.carconnect.service.*;
import com.hexaware.carconnect.service.AdminService;
import java.lang.Exception;


public class MainModule {

    public static void main(String[] args) { 
        Scanner scanner = new Scanner(System.in);

        try {
            CustomerService customerService = new CustomerService();
            VehicleService vehicleService = new VehicleService();
            ReservationService reservationService = new ReservationService();
            AdminService adminService = new AdminService();
            AuthenticationService authService = new AuthenticationService();
            
            System.out.println("Welcome to CarConnect!");

            while (true) {
                System.out.println("\nSelect an option:");
                System.out.println("1. Customer Management");
                System.out.println("2. Admin Login");
                System.out.println("3. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        customerManagement(scanner, customerService, authService, vehicleService, reservationService);
                        break;

                    case 2:
                        System.out.print("Enter username: ");
                        String adminUsername = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String adminPassword = scanner.nextLine();

                        try {
                            Admin admin = authService.authenticateAdmin(adminUsername, adminPassword);
                            System.out.println("Admin authenticated successfully: " + admin.getFirstName());

                            while (true) {
                                System.out.println("\nAdmin Menu:");
                                System.out.println("1. View All Reservations");
                                System.out.println("2. View All Customers");
                                System.out.println("3. Vehicle Management");
                                System.out.println("4. Generate Reports");
                                System.out.println("5. Logout");

                                int adminChoice = scanner.nextInt();
                                scanner.nextLine();

                                switch (adminChoice) {
                                    case 1:
                                        List<Reservation> allReservations = adminService.getAllReservations();
                                        if (allReservations.isEmpty()) {
                                            System.out.println("No reservations found.");
                                        } else {
                                            System.out.println("\nAll Reservations:");
                                            for (Reservation r : allReservations) {
                                                System.out.printf(
                                                        "Reservation ID: %d, Customer ID: %d, Vehicle ID: %d, Start: %s, End: %s, Status: %s, Total Cost: $%.2f%n",
                                                        r.getReservationID(),
                                                        r.getCustomerID(),
                                                        r.getVehicleID(),
                                                        r.getStartDate(),
                                                        r.getEndDate(),
                                                        r.getStatus(),
                                                        r.getTotalCost()
                                                );
                                            }
                                        }
                                        break;
                                    case 2:
                                        List<Customer> allCustomers = adminService.getAllCustomers();
                                        if (allCustomers.isEmpty()) {
                                            System.out.println("No customers found.");
                                        } else {
                                            System.out.println("\nRegistered Customers:");
                                            for (Customer c : allCustomers) {
                                                System.out.printf(
                                                        "Customer ID: %d, Name: %s %s, Username: %s, Email: %s%n",
                                                        c.getCustomerID(),
                                                        c.getFirstName(),
                                                        c.getLastName(),
                                                        c.getUsername(),
                                                        c.getEmail()
                                                );
                                            }
                                        }
                                        break;
                                    case 3:
                                        manageVehicles(scanner, vehicleService);
                                        break;
                                    case 4:
                                        generateReportsMenu(scanner, adminService); 
                                        break;
                                    case 5:
                                        System.out.println("Logging out...");
                                        break;

                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                }

                                if (adminChoice == 5) break; 
                            }

                        } catch (AuthenticationException e) {
                            System.out.println("Authentication failed: " + e.getMessage());
                        }
                        break;

                    case 3:
                        System.out.println("Exiting... Thank you for using CarConnect!");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (DatabaseConnectionException | AuthenticationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void customerManagement(Scanner scanner, CustomerService customerService, AuthenticationService authService,
                                           VehicleService vehicleService, ReservationService reservationService) {
        while (true) {
            System.out.println("\nCustomer Management:");
            System.out.println("1. Register as Customer");
            System.out.println("2. Login as Customer");
            System.out.println("3. Update Customer Info");
            System.out.println("4. Delete Customer");
            System.out.println("5. Back to Main Menu");

            int customerChoice = scanner.nextInt();
            scanner.nextLine();

            switch (customerChoice) {
                case 1:
                    registerCustomer(scanner, customerService);
                    break;
                case 2:
                    customerLogin(scanner, authService, vehicleService, reservationService);
                    break;
                case 3:
                    updateCustomerInfo(scanner, customerService, customerChoice);
                    break;
                case 4:
                    deleteCustomer(scanner, customerService);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    public static void registerCustomer(Scanner scanner, CustomerService customerService) {
        System.out.println("Register as a new customer");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
       

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setEmail(email);
        newCustomer.setPhoneNumber(phoneNumber);
        newCustomer.setAddress(address);
        newCustomer.setUsername(username);
        newCustomer.setPassword(password);
        
        newCustomer.setRegistrationDate(LocalDate.now());

        try {
            boolean isRegistered = customerService.registerCustomer(newCustomer);
            if (isRegistered) {
                System.out.println("Registration successful! You can now login.");
            } else {
                System.out.println("Registration failed. Username or email may already exist.");
            }
        } catch (Exception e) {
            System.out.println("Registration failed. Username or email may already exist.");
        }
    }

    public static void customerLogin(Scanner scanner, AuthenticationService authService,
                                      VehicleService vehicleService, ReservationService reservationService) {
        System.out.print("Enter username: ");
        String custUsername = scanner.nextLine();
        System.out.print("Enter password: ");
        String custPassword = scanner.nextLine();

        try {
            Customer customer = authService.authenticateCustomer(custUsername, custPassword);
            System.out.println("Customer authenticated successfully: " + customer.getFirstName());

            while (true) {
                System.out.println("\nCustomer Menu:");
                System.out.println("1. View Available Vehicles");
                System.out.println("2. Make a Reservation");
                System.out.println("3. View My Reservations");
                System.out.println("4. Logout");

                int custChoice = scanner.nextInt();
                scanner.nextLine();

                switch (custChoice) {
                    case 1:
                        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
                        System.out.println("\nAvailable Vehicles:");
                        for (Vehicle v : vehicles) {
                            System.out.printf("ID: %d, Make: %s, Model: %s, Year: %d, Price/Day: %.2f%n",
                                    v.getVehicleID(), v.getMake(), v.getModel(), v.getYear(), v.getDailyRate());
                        }
                        break;
                    case 2:
                        makeReservation(scanner, customer, reservationService, vehicleService);
                        break;
                    case 3:
                        viewMyReservations(customer, reservationService);
                        break;
                    case 4:
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }

                if (custChoice == 4) break;
            }

        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
        }
    }

    public static void updateCustomerInfo(Scanner scanner, CustomerService customerService, int customerChoice) {
        System.out.println("Enter your Customer ID for verification:");
        int customerId = scanner.nextInt();
        scanner.nextLine(); 

        System.out.println("Enter updated first name: ");
        String updatedFirstName = scanner.nextLine();

        System.out.println("Enter updated last name: ");
        String updatedLastName = scanner.nextLine();

        System.out.println("Enter updated email: ");
        String updatedEmail = scanner.nextLine();
        
        System.out.print("Enter Updated phone number: ");
        String updatedPhoneNumber = scanner.nextLine();
        
        System.out.print("Enter Updated address: ");
        String updatedAddress = scanner.nextLine();

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerID(customerId);
        updatedCustomer.setFirstName(updatedFirstName);
        updatedCustomer.setLastName(updatedLastName);
        updatedCustomer.setEmail(updatedEmail);
        updatedCustomer.setPhoneNumber(updatedPhoneNumber);
        updatedCustomer.setAddress(updatedAddress);

        try {
            boolean isUpdated = customerService.updateCustomer(updatedCustomer);
            if (isUpdated) {
                System.out.println("Customer information updated successfully.");
            } else {
                System.out.println("Update failed. Customer ID may not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error updating customer: " + e.getMessage());
        }
    }



    public static void deleteCustomer(Scanner scanner, CustomerService customerService) {

        System.out.println("Deleting customer...");

        
        System.out.print("Enter the customer ID to delete: ");
        int customerId = scanner.nextInt();
        scanner.nextLine();  

        
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;  
        }

        
        System.out.println("Are you sure you want to delete the following customer?");
        System.out.println("Customer ID: " + customer.getCustomerID());
        System.out.println("Name: " + customer.getFirstName() + " " + customer.getLastName());
        System.out.println("Email: " + customer.getEmail());
        System.out.println("Phone: " + customer.getPhoneNumber());
        System.out.print("Enter 'yes' to confirm, or 'no' to cancel: ");
        String confirmation = scanner.nextLine();

        
        if ("yes".equalsIgnoreCase(confirmation)) {
            try {
                
                boolean reservationsDeleted = customerService.deleteReservationsByCustomerId(customerId);
                if (!reservationsDeleted) {
                    System.out.println("Error: Unable to delete associated reservations.");
                    return;
                }

                
                boolean success = customerService.deleteCustomer(customerId);
                if (success) {
                    System.out.println("Customer deleted successfully.");
                } else {
                    System.out.println("Failed to delete customer.");
                }
            } catch (Exception e) {
                
                System.out.println("Error: Unable to delete customer. There may be existing reservations associated with this customer.");
                e.printStackTrace();  
            }
        } else {
            System.out.println("Customer deletion canceled.");
        }
    }


    

    public static void manageVehicles(Scanner scanner, VehicleService vehicleService) {
        while (true) {
            System.out.println("\nVehicle Management Menu:");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Update Vehicle");
            System.out.println("3. Delete Vehicle");
            System.out.println("4. Back to Admin Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter make: ");
                        String make = scanner.nextLine();
                        
                        System.out.print("Enter model: ");
                        String model = scanner.nextLine();
                        
                        System.out.print("Enter year: ");
                        int year = scanner.nextInt();
                        scanner.nextLine();
                        
                        
                        System.out.print("Enter colour: ");
                        String color = scanner.nextLine();
                        
                        System.out.print("Enter registrationNumber: ");
                        String registrationNumber = scanner.nextLine();
                        
                        System.out.print("Is the vehicle available? (true/false): ");
                        boolean availability = scanner.nextBoolean();
                        
                        System.out.print("Enter daily rate: ");
                        double rate = scanner.nextDouble();
                        
                        scanner.nextLine();

                        Vehicle newVehicle = new Vehicle();
                        newVehicle.setMake(make);
                        newVehicle.setModel(model);
                        newVehicle.setYear(year);
                        newVehicle.setColor(color);
                        newVehicle.setRegistrationNumber(registrationNumber);
                        newVehicle.setAvailable(availability);
                        newVehicle.setDailyRate(rate);

                        boolean added = vehicleService.addVehicle(newVehicle);
                        System.out.println(added ? "Vehicle added successfully." : "Failed to add vehicle.");
                        break;

                    case 2:
                        System.out.print("Enter vehicle ID to update: ");
                        int vIdToUpdate = scanner.nextInt();
                        scanner.nextLine(); 

                        System.out.print("Enter updated make: ");
                        String updatedMake = scanner.nextLine();

                        System.out.print("Enter updated model: ");
                        String updatedModel = scanner.nextLine();

                        System.out.print("Enter updated year: ");
                        int updatedYear = scanner.nextInt();
                        scanner.nextLine(); 

                        System.out.print("Enter updated color: ");
                        String updatedColor = scanner.nextLine();

                        System.out.print("Enter updated registration number: ");
                        String updatedRegNo = scanner.nextLine();

                        System.out.print("Is the vehicle available? (true/false): ");
                        boolean updatedAvailability = scanner.nextBoolean();

                        System.out.print("Enter updated daily rate: ");
                        double updatedRate = scanner.nextDouble();
                        scanner.nextLine(); 

                        Vehicle updatedVehicle = new Vehicle();
                        updatedVehicle.setVehicleID(vIdToUpdate);
                        updatedVehicle.setMake(updatedMake);
                        updatedVehicle.setModel(updatedModel);
                        updatedVehicle.setYear(updatedYear);
                        updatedVehicle.setColor(updatedColor);
                        updatedVehicle.setRegistrationNumber(updatedRegNo);
                        updatedVehicle.setAvailable(updatedAvailability);
                        updatedVehicle.setDailyRate(updatedRate);

                        boolean updated = vehicleService.updateVehicle(updatedVehicle);
                        System.out.println(updated ? "Vehicle updated successfully." : "Vehicle not found or update failed.");
                        break;


                    case 3:
                        System.out.print("Enter vehicle ID to delete: ");
                        int vIdToDelete = scanner.nextInt();
                        scanner.nextLine();

                        boolean deleted = vehicleService.removeVehicle(vIdToDelete);
                        System.out.println(deleted ? "Vehicle deleted successfully." : "Vehicle not found.");
                        break;

                    case 4:
                        return;

                    default:
                        System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }
    }

    public static void generateReportsMenu(Scanner scanner, AdminService adminService) {
        while (true) {
            System.out.println("\nReport Generation Menu:");
            System.out.println("1. Reservation History");
            System.out.println("2. Vehicle Utilization");
            System.out.println("3. Revenue Report");
            System.out.println("4. Back to Admin Menu");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    
                	try {
                        adminService.generateReservationHistoryReport();
                        System.out.println("Reservation History Report generated successfully.");
                    } catch (Exception e) {
                        System.out.println("Error generating Reservation History Report: " + e.getMessage());
                    }
                    break;
                case 2:
                    
                	try {
                        adminService.generateVehicleUtilizationReport();
                        System.out.println("Vehicle Utilization Report generated successfully.");
                    } catch (Exception e) {
                        System.out.println("Error generating Vehicle Utilization Report: " + e.getMessage());
                    }
                    break;
                case 3:
                    
                	try {
                        adminService.generateRevenueReport();
                        System.out.println("Revenue Report generated successfully.");
                    } catch (Exception e) {
                        System.out.println("Error generating Revenue Report: " + e.getMessage());
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static void makeReservation(Scanner scanner, Customer customer, ReservationService reservationService, VehicleService vehicleService) {
        try {
            List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
            if (vehicles.isEmpty()) {
                System.out.println("No vehicles available for reservation.");
                return;
            }

            System.out.println("\nAvailable Vehicles:");
            for (Vehicle v : vehicles) {
                System.out.printf("ID: %d, Make: %s, Model: %s, Year: %d, Price/Day: %.2f%n",
                        v.getVehicleID(), v.getMake(), v.getModel(), v.getYear(), v.getDailyRate());
            }

            System.out.print("Enter Vehicle ID to reserve: ");
            int vehicleId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter start date (YYYY-MM-DD): ");
            String startDate = scanner.nextLine();

            System.out.print("Enter end date (YYYY-MM-DD): ");
            String endDate = scanner.nextLine();

            boolean success = reservationService.makeReservation(customer.getCustomerID(), vehicleId, startDate, endDate);

            if (success) {
                System.out.println("Reservation successful!");

                String subject = "CarConnect Reservation Confirmation";
                String body = "Dear " + customer.getFirstName() + ",\n\n"
                    + "Your reservation for Vehicle ID " + vehicleId + " from " + startDate + " to " + endDate + " has been confirmed.\n"
                    + "Thank you for choosing CarConnect!\n\n"
                    + "Best regards,\nCarConnect Team";

                try {
                    EmailUtil.sendEmail(customer.getEmail(), subject, body);
                } catch (MessagingException e) {
                    System.out.println("Failed to send confirmation email: " + e.getMessage());
                }
            } else {
                System.out.println("Reservation failed. Please check vehicle availability or try again.");
            }


        } catch (Exception e) {
            System.out.println("Error during reservation: " + e.getMessage());
        }
    }

    public static void viewMyReservations(Customer customer, ReservationService reservationService) {
        try {
            List<Reservation> reservations = reservationService.getReservationsByCustomer(customer.getCustomerID());

            if (reservations.isEmpty()) {
                System.out.println("You have no reservations.");
            } else {
                System.out.println("\nYour Reservations:");
                for (Reservation r : reservations) {
                    System.out.printf(
                            "Reservation ID: %d, Vehicle ID: %d, Start: %s, End: %s, Status: %s, Total Cost: $%.2f%n",
                            r.getReservationID(),
                            r.getVehicleID(),
                            r.getStartDate(),
                            r.getEndDate(),
                            r.getStatus(),
                            r.getTotalCost()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching reservations: " + e.getMessage());
        }
    }
}
