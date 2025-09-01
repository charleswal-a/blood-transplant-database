import java.util.Comparator;

public class NumConnectionsComparator implements Comparator<Patient> {
    /** Represents the transplant system the patients that are being compared are in. */
    private TransplantGraph tg;

    /**
     * This Constructor method is used to create a new NunConnectionsComparator object.
     *
     * @param tg
     * The transplant system the patients that are being compared are in.
     */
    public NumConnectionsComparator(TransplantGraph tg) {
        this.tg = tg;
    }

    /**
     * This method is used to compare two Patient objects based on their number of connections.
     *
     * @param p1
     * The first patient to be compared.
     * @param p2
     * The second patient to be compared.
     *
     * @return
     * An int representing the relationship between the two objects' number of connections.
     */
    public int compare(Patient p1, Patient p2) {
        int p1Connections = countConnections(p1);
        int p2Connections = countConnections(p2);
        return Integer.compare(p1Connections, p2Connections);
    }

    /**
     * This method is used to count the number of connections that a patient has.
     *
     * @param p
     * The patient to count the connections of.
     *
     * @return
     * An int representing the number of connections that a patient has.
     */
    private int countConnections(Patient p) {
        tg.updateConnections();
        int numConnections = 0;
        if (p.isDonor()) {
            for (int j = 0; j < tg.getRecipients().size(); j++) {
                if (tg.getConnections()[tg.getDonors().indexOf(p)][j]) {
                    numConnections++;
                }
            }
        } else {
            for (int i = 0; i < tg.getDonors().size(); i++) {
                if (tg.getConnections()[i][tg.getRecipients().indexOf(p)]) {
                    numConnections++;
                }
            }
        }
        return numConnections;
    }
}
