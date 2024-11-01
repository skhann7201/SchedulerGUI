package com.example.project3;

/**
 * The Technician class extends the Provider class.
 * It represents a technician in the clinic management system,
 * including information about their profile, location, and rate per visit.
 *
 * @author Vy Nguyen
 */
public class Technician extends Provider {
    // Instance variable
    private int ratePerVisit; // keep track of technician's charging rate

    /**
     * Default constructor for the Technician class.
     * This initializes a new Technician instance
     * and sets the rate per visit to 0.
     */
    public Technician () {
        super();
        this.ratePerVisit = 0;
    }

    /**
     * Constructs a Technician object with the specified profile, location, and
     * rate per visit.
     *
     * @param profile the profile of the technician
     * @param location the location where the technician operates
     * @param ratePervisit the rate charged by the technician per visit
     */
    public Technician(Profile profile, Location location, int ratePervisit) {
        super(profile,location);
        this.ratePerVisit = ratePervisit;
    }

    /**
     * Returns the charging rate per visit for the technician.
     * @return the rate per visit.
     */
    @Override
    public int rate() {
        return this.ratePerVisit;
    }

    /**
     * Returns a string representation of the Technician object.
     * The representation includes the provider details as given by
     * the superclass and the rate per visit for the technician.
     *
     * @return a string representation of the Technician object.
     */
    @Override
    public String toString() {
        return "[%s][rate: %.2f]".formatted( super.toString(), (double)this.ratePerVisit);
    }
}

