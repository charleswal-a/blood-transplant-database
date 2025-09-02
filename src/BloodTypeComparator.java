import java.util.Comparator;

/**
 * This class represents a comparator used to compare two Patient objects based on blood type.
 *
 * @author Charles Walford
 */
class BloodTypeComparator implements Comparator<Patient> {
    /**
     * This method is used to compare two Patient objects based on their blood types.
     *
     * @param p1
     * The first patient to be compared.
     * @param p2
     * The second patient to be compared.
     *
     * @return
     * An int representing the relationship between the two objects' blood types.
     */
    public int compare(Patient p1, Patient p2) {
        return p1.getBloodType().getBloodType().compareTo(p2.getBloodType().getBloodType());
    }
}
