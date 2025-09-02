import java.io.Serializable;

/**
 * This class represents a patient in the transplant system.
 *
 * @author Charles Walford
 */
public class Patient implements Serializable, Comparable<Object> {
    /** Represents the name of this patient. */
    private String name;
    /** Represents the organ for this patient to receive/donate. */
    private String organ;
    /** Represents the age of this patient. */
    private int age;
    /** Represents the blood type of this patient. */
    private BloodType bloodType;
    /** Represents the ID of this patient. */
    private int ID;
    /** Represents if this patient is a donor or not. */
    private boolean isDonor;

    /**
     * This is a Constructor method that is used to create a new Patient object.
     */
    public Patient() {
        name = "";
        organ = "";
        age = 0;
        bloodType = null;
        ID = 0;
        isDonor = false;
    }

    /**
     * This is a Constructor method that is used to create a new Patient object with parameters.
     *
     * @param name
     * The name of this patient.
     * @param organ
     * The organ for this patient to receive or donate.
     * @param age
     * The age of this patient.
     * @param bloodType
     * The blood type of this patient.
     * @param ID
     * The ID of this patient.
     * @param isDonor
     * Represents if this patient is a donor.
     */
    public Patient(String name, String organ, int age, BloodType bloodType, int ID, boolean isDonor) {
        this.name = name;
        this.organ = organ;
        this.age = age;
        this.bloodType = bloodType;
        this.ID = ID;
        this.isDonor = isDonor;
    }

    /**
     * This method is used to compare this Patient with another object based on ID.
     *
     * @param o
     * The object to be compared.
     *
     * @return
     * An int representing the relationship between the two objects' IDs.
     */
    public int compareTo(Object o) {
        Patient otherPatient = (Patient) o;
        if (this.ID == otherPatient.ID) {
            return 0;
        } else if (this.ID > otherPatient.ID) {
            return 1;
        }
        return -1;
    }

    /**
     * This method constructs a formatted String containing the information about this Patient object.
     *
     * @return
     * A String containing the information about this Patient object.
     */
    public String toString() {
        String output = "   " + String.format("%-3d", ID);
        output += "| " + String.format("%-19s", name);
        output += "| " + String.format("%-4d", age);
        output += "|  " + String.format("%-13s", organ);
        output += "|      " + String.format("%-6s", bloodType.getBloodType()) + "| ";
        return output;
    }

    /**
     * This Getter method is used to get the name of this patient.
     *
     * @return
     * A String representing the name of this patient.
     */
    public String getName() {
        return name;
    }

    /**
     * This Getter method is used to get the organ of this patient.
     *
     * @return
     * A String representing the organ of this patient.
     */
    public String getOrgan() {
        return organ;
    }

    /**
     * This Getter method is used to get the age of this patient.
     *
     * @return
     * An int representing the age of this patient.
     */
    public int getAge() {
        return age;
    }

    /**
     * This Getter method is used to get the blood type of this patient.
     *
     * @return
     * A BloodType object representing the blood type of this patient.
     */
    public BloodType getBloodType() {
        return bloodType;
    }

    /**
     * This Getter method is used to get the ID of this patient.
     *
     * @return
     * An int representing the ID of this patient.
     */
    public int getID() {
        return ID;
    }

    /**
     * This Setter method is used to set the ID of this patient.
     *
     * @param ID
     * The new ID for this patient.
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * This Getter method is used to get if this patient is a donor.
     *
     * @return
     * A boolean representing if this patient is a donor.
     */
    public boolean isDonor() {
        return isDonor;
    }
}
