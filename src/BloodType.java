import java.io.Serializable;

/**
 * This class represents a blood type that a patient has.
 *
 * @author Charles Walford
 * Solar ID: 116237064
 * Email: charles.walford@stonybrook.edu
 * Assignment number: 7
 * Course: CSE 214
 * Recitation number: 1
 * TAs: Yvette Han, Vincent Zheng
 */
public class BloodType implements Serializable {
    /** Represents the blood type of the patient.*/
    private String bloodType;

    /**
     * This is a Constructor method that is used to create a new BloodType object.
     */
    public BloodType(){
        bloodType = "";
    }

    /**
     * This is a Constructor method that is used to create a new BloodType object with parameters.
     *
     * @param bloodType
     * The blood type of the patient.
     */
    public BloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * This method is used to find out if two blood types are compatible.
     *
     * @param donor
     * The blood type of the donor.
     * @param recipient
     * The blood type of the recipient.
     *
     * @return
     * A boolean representing if the two blood types are compatible.
     */
    public static boolean isCompatible(BloodType donor, BloodType recipient) {
        if (donor.getBloodType().equals(recipient.getBloodType())) {
            return true;
        } else if (donor.getBloodType().equals("O")) {
            return true;
        } else return recipient.getBloodType().equals("AB");
    }

    /**
     * This Getter method is used get the blood type of the patient.
     *
     * @return
     * A String representing the blood type of the patient
     */
    public String getBloodType() {
        return bloodType;
    }
}
