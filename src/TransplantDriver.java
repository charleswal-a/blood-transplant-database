import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * This class represents the Transplant Driver the user can use to interact with the transplant system.
 *
 * @author Charles Walford
 * Solar ID: 116237064
 * Email: charles.walford@stonybrook.edu
 * Assignment number: 7
 * Course: CSE 214
 * Recitation number: 1
 * TAs: Yvette Han, Vincent Zheng
 */
public class TransplantDriver {
    /** Represents the file path containing information on the donors. */
    public static final String DONOR_FILE = "donors.txt";
    /** Represents the file path containing information on the recipients. */
    public static final String RECIPIENT_FILE = "recipients.txt";

    /**
     * This is a main method that runs the transplant system and handles user inputs.
     *
     * @param args
     * The command line arguments.
     */
    public static void main(String[] args) {
        boolean done = false;
        Scanner s = new Scanner(System.in);
        TransplantGraph tg;

        try {
            FileInputStream file = new FileInputStream("transplant.obj");
            ObjectInputStream input = new ObjectInputStream(file);
            System.out.println("TransplantGraph is loaded from transplant.obj");
            tg = (TransplantGraph) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("transplant.obj not found. Creating new TransplantGraph object...\n" +
                    "Loading data from 'donors.txt'...\n" +
                    "Loading data from 'recipients.txt'...");
            tg = TransplantGraph.buildFromFiles(DONOR_FILE, RECIPIENT_FILE);
        }

        while (!done) {
            System.out.print("\nMenu:\n" +
                    "(LR) - List all recipients\n" +
                    "(LO) - List all donors\n" +
                    "(AO) - Add new donor\n" +
                    "(AR) - Add new recipient\n" +
                    "(RO) - Remove donor\n" +
                    "(RR) - Remove recipient\n" +
                    "(SR) - Sort recipients\n" +
                    "(SO) - Sort donors\n" +
                    "(Q) - Quit\n" +
                    "\nPlease select an option:");
            String choice = s.nextLine().toUpperCase();

            if (choice.equals("LR")) {
                tg.printAllRecipients();
            } else if (choice.equals("LO")) {
                tg.printAllDonors();
            } else if (choice.equals("AO")) {
                System.out.print("\nPlease enter the organ donor name: ");
                String name = s.nextLine();
                System.out.print("Please enter the blood type of " + name + ": ");
                String bloodType = s.nextLine();
                System.out.print("Please enter the age of " + name + ": ");
                String age = s.nextLine();
                System.out.print("Please enter the organs " + name + " is donating: ");
                String organ = s.nextLine();

                String[] validBloodTypes = {"A", "B", "O", "AB"};
                if (isInteger(age) && Arrays.asList(validBloodTypes).contains(bloodType)) {
                    Patient newDonor = new Patient(name, organ, Integer.parseInt(age), new BloodType(bloodType), tg.getDonors().size(), true);
                    tg.addDonor(newDonor);
                    System.out.println("\nThe organ donor, " + name + " , has been added to the donor list with ID " + newDonor.getID() + ".");
                } else {
                    System.out.println("\nInvalid inputs.");
                }
            } else if (choice.equals("AR")) {
                System.out.print("\nPlease enter new recipient's name: ");
                String name = s.nextLine();
                System.out.print("Please enter the recipient's blood type: ");
                String bloodType = s.nextLine();
                System.out.print("Please enter the recipient's age: ");
                String age = s.nextLine();
                System.out.print("Please enter the organ needed: ");
                String organ = s.nextLine();

                String[] validBloodTypes = {"A", "B", "O", "AB"};
                if (isInteger(age) && Arrays.asList(validBloodTypes).contains(bloodType)) {
                    Patient newRecipient = new Patient(name, organ, Integer.parseInt(age), new BloodType(bloodType), tg.getRecipients().size(), false);
                    tg.addRecipient(newRecipient);
                    System.out.println("\nThe organ recipient, " + name + " , has been added to the recipient list with ID " + newRecipient.getID() + ".");
                } else {
                    System.out.println("\nInvalid inputs.");
                }
            } else if (choice.equals("RO")) {
                System.out.print("\nPlease enter the name of the organ donor to remove: ");
                String name = s.nextLine();
                tg.removeDonor(name);
            } else if (choice.equals("RR")) {
                System.out.print("\nPlease enter the name of the recipient to remove: ");
                String name = s.nextLine();
                tg.removeRecipient(name);
            } else if (choice.equals("SR")) {
                boolean sorting = true;
                ArrayList<Patient> originalRecipients = new ArrayList<Patient>();
                for (Patient p : tg.getRecipients()) {
                    originalRecipients.add(new Patient(p.getName(), p.getOrgan(), p.getAge(), new BloodType(p.getBloodType().getBloodType()), p.getID(), p.isDonor()));
                }

                while (sorting) {
                    System.out.println("\n    (I) Sort by ID\n" +
                            "    (N) Sort by Number of Donors\n" +
                            "    (B) Sort by Blood Type\n" +
                            "    (O) Sort by Organ Needed\n" +
                            "    (Q) Back to Main Menu\n\n" +
                            "Please select an option: ");
                    choice = s.nextLine().toUpperCase();

                    if (choice.equals("I")) {
                        tg.sortByID("recipients");
                        tg.printAllRecipients();
                    } else if (choice.equals("N")) {
                        tg.sortRecipients(new NumConnectionsComparator(tg));
                        tg.printAllRecipients();
                    } else if (choice.equals("B")) {
                        tg.sortRecipients(new BloodTypeComparator());
                        tg.printAllRecipients();
                    } else if (choice.equals("O")) {
                        tg.sortRecipients(new OrganComparator());
                        tg.printAllRecipients();
                    } else if (choice.equals("Q")) {
                        System.out.println("\nReturning to main menu. ");
                        sorting = false;
                    } else {
                        System.out.println("\nInvalid input.");
                    }
                }
                tg.restoreRecipients(originalRecipients);
            } else if (choice.equals("SO")) {
                boolean sorting = true;
                ArrayList<Patient> originalDonors = new ArrayList<Patient>();
                for (Patient p : tg.getDonors()) {
                    originalDonors.add(new Patient(p.getName(), p.getOrgan(), p.getAge(), new BloodType(p.getBloodType().getBloodType()), p.getID(), p.isDonor()));
                }

                while (sorting) {
                    System.out.println("\n    (I) Sort by ID\n" +
                            "    (N) Sort by Number of Donors\n" +
                            "    (B) Sort by Blood Type\n" +
                            "    (O) Sort by Organ Needed\n" +
                            "    (Q) Back to Main Menu\n\n" +
                            "Please select an option: ");
                    choice = s.nextLine().toUpperCase();

                    if (choice.equals("I")) {
                        tg.sortByID("donors");
                        tg.printAllDonors();
                    } else if (choice.equals("N")) {
                        tg.sortDonors(new NumConnectionsComparator(tg));
                        tg.printAllDonors();
                    } else if (choice.equals("B")) {
                        tg.sortDonors(new BloodTypeComparator());
                        tg.printAllDonors();
                    } else if (choice.equals("O")) {
                        tg.sortDonors(new OrganComparator());
                        tg.printAllDonors();
                    } else if (choice.equals("Q")) {
                        System.out.println("\nReturning to main menu. ");
                        sorting = false;
                    } else {
                        System.out.println("\nInvalid input.");
                    }
                }
                tg.restoreDonors(originalDonors);
            } else if (choice.equals("Q")) {
                done = true;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        try {
            FileOutputStream file = new FileOutputStream("transplant.obj");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(tg);
            out.close();
            System.out.println("\nWriting data to transplant.obj...");
        } catch (IOException e) {
            System.out.println("Error saving transplant data: " + e.getMessage());
        }
        System.out.println("\nProgram terminating normally...");
    }

    /**
     * This method determines if an input is a valid integer
     *
     * @param input
     * The input to determine if it is an integer
     *
     * @return
     * A boolean that represents if input is a valid integer
     *
     * @throws NumberFormatException
     * when the input is not a valid integer
     */
    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
