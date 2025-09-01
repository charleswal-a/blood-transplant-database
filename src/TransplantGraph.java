import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * This class represents the transplant system.
 *
 * @author Charles Walford
 * Solar ID: 116237064
 * Email: charles.walford@stonybrook.edu
 * Assignment number: 7
 * Course: CSE 214
 * Recitation number: 1
 * TAs: Yvette Han, Vincent Zheng
 */
public class TransplantGraph implements Serializable {
    /** Represents the donors in the transplant system. */
    private ArrayList<Patient> donors;
    /** Represents the recipients in the transplant system. */
    private ArrayList<Patient> recipients;
    /** Represent the maximum amount of patients the system can hold. */
    public static final int MAX_PATIENTS = 100;
    /** Represents the connections between the donors and recipients. */
    private boolean[][] connections;

    /**
     * This Constructor method is used to create a new TransplantGraph object.
     */
    public TransplantGraph() {
        donors = new ArrayList<>();
        recipients = new ArrayList<>();
        connections = new boolean[MAX_PATIENTS][MAX_PATIENTS];
    }

    /**
     * This method is used to build a new TransplantGraph from the given files.
     *
     * @param donorFile
     * The file path of the file containing the information about the donors.
     * @param recipientFile
     * The file path of the file containing the information about the recipients.
     *
     * @return
     * A TransplantGraph object that contains the information from the files.
     */
    public static TransplantGraph buildFromFiles(String donorFile, String recipientFile) {
        TransplantGraph tg = new TransplantGraph();

        try {
            FileInputStream file = new FileInputStream(donorFile);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] elements = line.split(", ");
                String organ = elements[3].substring(0, 1).toUpperCase() + elements[3].substring(1);
                Patient donor = new Patient(elements[1], organ, Integer.parseInt(elements[2]), new BloodType(elements[4]), Integer.parseInt(elements[0]), true);
                tg.addDonor(donor);
            }

            file = new FileInputStream(recipientFile);
            scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] elements = line.split(", ");
                Patient recipient = new Patient(elements[1], elements[3], Integer.parseInt(elements[2]), new BloodType(elements[4]), Integer.parseInt(elements[0]), false);
                tg.addRecipient(recipient);
            }
            file.close();
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error reading files: " + e.getMessage());
        }
        return tg;
    }

    /**
     * This method is used to add a new recipient to the transplant system.
     *
     * @param patient
     * The new recipient to add to the transplant system.
     */
    public void addRecipient(Patient patient) {
        recipients.add(patient);
        for (int i = 0; i < donors.size(); i++) {
            connections[i][patient.getID()] = findCompatibility(donors.get(i), patient);
        }
    }

    /**
     * This method is used to add a new donor to the transplant system.
     *
     * @param patient
     * The new donor to add to the transplant system.
     */
    public void addDonor(Patient patient) {
        donors.add(patient);
        for (int i = 0; i < recipients.size(); i++) {
            connections[patient.getID()][i] = findCompatibility(patient, recipients.get(i));
        }
    }

    /**
     * This method is used to remove a recipient from the transplant system.
     *
     * @param name
     * The name of the patient to remove from the transplant system.
     */
    public void removeRecipient(String name) {
        int index = -1;
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getName().equals(name)) {
                index = i;
            }
        }

        if (index != -1) {
            recipients.remove(index);
            for (int i = 0; i < donors.size(); i++) {
                for (int j = index; j < recipients.size(); j++) {
                    connections[i][j] = connections[i][j+1];
                }
                if (!recipients.isEmpty()) {
                    connections[i][recipients.size()] = false;
                }
            }
            for (int i = index; i < recipients.size(); i++) {
                recipients.get(i).setID(i);
            }
            System.out.println("\n" + name + " was removed from the organ transplant waitlist.");
        } else {
            System.out.println("Failed to remove recipient: No such patient named " + name + " in list of recipients.");
        }
    }

    /**
     * This method is used to remove a donor from the transplant system.
     *
     * @param name
     * The name of the donor to remove from the transplant system.
     */
    public void removeDonor(String name) {
        int index = -1;
        for (int i = 0; i < donors.size(); i++) {
            if (donors.get(i).getName().equals(name)) {
                index = i;
            }
        }

        if (index != -1) {
            donors.remove(index);
            for (int i = 0; i < donors.size(); i++) {
                for (int j = index; j < recipients.size(); j++) {
                    connections[i][j] = connections[i][j + 1];
                }
            }

            if (!donors.isEmpty()) {
                for (int i = index; i < recipients.size(); i++) {
                    connections[donors.size()][i] = false;
                }
            }

            for (int i = index; i < donors.size(); i++) {
                donors.get(i).setID(i);
            }
            System.out.println("\n" + name + " was removed from the organ donor list.");
        } else {
            System.out.println("Failed to remove donor: No such patient named " + name + " in list of donors.");
        }
    }

    /**
     * This method is used to print all the recipients in the transplant system.
     */
    public void printAllRecipients() {
        System.out.println("\nIndex | Recipient Name     | Age | Organ Needed  | Blood Type | Donor ID\n" +
                "========================================================================");
        for (Patient recipient : recipients) {
            System.out.print(recipient);
            String donorList = "";
            for (Patient donor : donors) {
                if (findCompatibility(donor, recipient)) {
                    donorList += donor.getID() + ", ";
                }
            }
            if (!donorList.isEmpty()) {
                donorList = donorList.substring(0, donorList.length() - 2);
            }
            System.out.println(donorList);
        }
    }

    /**
     * This method is used to print all the donors in the transplant system.
     */
    public void printAllDonors() {
        System.out.println("\nIndex | Donor Name         | Age | Organ Donated | Blood Type | Recipient IDs\n" +
                "=============================================================================");
        for (Patient donor : donors) {
            System.out.print(donor);
            String recipientList = "";
            for (Patient recipient : recipients) {
                if (findCompatibility(donor, recipient)) {
                    recipientList += recipient.getID() + ", ";
                }
            }
            if (!recipientList.isEmpty()) {
                recipientList = recipientList.substring(0, recipientList.length() - 2);
            }
            System.out.println(recipientList);
        }
    }

    /**
     * This method is used to sort the recipients based on a given comparator.
     *
     * @param c
     * The comparator to sort the recipients based on.
     */
    public void sortRecipients(Comparator<Patient> c) {
        Collections.sort(recipients);
        Collections.sort(recipients, c);
        updateConnections();
    }

    /**
     * This method is used to sort the donors based on a given comparator.
     *
     * @param c
     * The comparator to sort the donors based on.
     */
    public void sortDonors(Comparator<Patient> c) {
        Collections.sort(donors);
        Collections.sort(donors, c);
        updateConnections();
    }

    /**
     * This method is used to sort either the recipients or donors by ID.
     *
     * @param list
     * The word to indicate either recipients or donors to sort.
     */
    public void sortByID(String list) {
        if (list.equals("recipients")) {
            Collections.sort(recipients);
        } else if (list.equals("donors")) {
            Collections.sort(donors);
        }
    }

    /**
     * This method is used to restore the recipients and connections to a previous state.
     *
     * @param originalRecipients
     * The list of recipient to restore.
     */
    public void restoreRecipients(ArrayList<Patient> originalRecipients) {
        recipients.clear();
        recipients.addAll(originalRecipients);
        for (Patient donor : donors) {
            for (Patient recipient : recipients) {
                connections[donor.getID()][recipient.getID()] = findCompatibility(donor, recipient);
            }
        }
    }

    /**
     * This method is used to restore the donors and connections to a previous state.
     *
     * @param originalDonors
     * The list of donors to restore.
     */
    public void restoreDonors(ArrayList<Patient> originalDonors) {
        donors.clear();
        donors.addAll(originalDonors);
        for (Patient donor : donors) {
            for (Patient recipient : recipients) {
                connections[donor.getID()][recipient.getID()] = findCompatibility(donor, recipient);
            }
        }
    }

    /**
     * This method is used to find if a donor and recipient are compatible.
     *
     * @param donor
     * The donor to check.
     * @param recipient
     * The recipient to check.
     *
     * @return
     * A boolean representing if the donor and recipient are compatible.
     */
    public boolean findCompatibility(Patient donor, Patient recipient) {
        return BloodType.isCompatible(donor.getBloodType(), recipient.getBloodType()) && donor.getOrgan().equalsIgnoreCase(recipient.getOrgan());
    }

    /**
     * This method is used to update the connection matrix based on the donors and recipients.
     */
    public void updateConnections() {
        for (int i = 0; i < donors.size(); i++) {
            for (int j = 0; j < recipients.size(); j++) {
                connections[i][j] = findCompatibility(donors.get(i), recipients.get(j));
            }
        }
    }

    /**
     * This Getter method is used to get the list of donors.
     *
     * @return
     * An ArrayList representing the list of donors.
     */
    public ArrayList<Patient> getDonors() {
        return donors;
    }

    /**
     * This Getter method is used to get the list of recipients.
     *
     * @return
     * An ArrayList representing the list of recipients.
     */
    public ArrayList<Patient> getRecipients() {
        return recipients;
    }

    /**
     * This Getter method is used to get the connection matrix.
     *
     * @return
     * A boolean 2D array representing the connection matrix.
     */
    public boolean[][] getConnections() {
        return connections;
    }
}
