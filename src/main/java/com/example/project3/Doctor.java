package com.example.project3;

/**
 * The Doctor class represents a medical doctor in the clinic management system.
 * This class extends the Provider class and includes additional attributes for
 * the doctor's specialty and National Provider Identifier (NPI).
 *
 * @author Vy Nguyen
 */
public class Doctor extends Provider {
    // Instance variables
    private Specialty specialty; //encapsulate the rate per visit based on specialty
    private String npi; //National Provider Identification unique to the doctor

    /**
     * Default constructor
     */
    public Doctor() {
        super();
        this.specialty = null;
        this.npi = null;
    }

    /**
     * Constructor to create a Doctor instance.
     * @param profile    the profile of the doctor
     * @param location   the location of the doctor's practice
     * @param specialty the specialty of the doctor
     * @param npi       the National Provider Identifier for the doctor
     */
    public Doctor(Profile profile, Location location, Specialty specialty, String npi) {
        super(profile, location);
        this.specialty = specialty;
        this.npi = npi;
    }

    /**
     * Gets the National Provider Identifier for the doctor.
     *
     * @return the NPI of the doctor
     */
    public String getNPI() {
        return this.npi;
    }

    /**
     * Gets the specialty of the doctor.
     *
     * @return the specialty of the doctor
     */
    public Specialty getSpecialty() {
        return this.specialty;
    }

    /**
     * Implements the rate method from the Provider class.
     * Returns the doctor's charge rate per visit.
     * @return the charge rate for the doctor
     */
    @Override
    public int rate() {
        return specialty.getCharge();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Doctor) {
            Doctor doctor = (Doctor) obj;
            return this.profile.equals(doctor.profile) &&
                    this.getLocation().equals(doctor.getLocation()) &&
                    this.specialty.equals(doctor.specialty) &&
                    this.npi.equals(doctor.npi);
        }
        return false;
    }

    /**
     * Returns a string representation of the Doctor instance.
     * The string includes the provider's profile and location details,
     * followed by the doctor's specialty and National Provider Identifier (NPI).
     *
     * @return a string representing the details of the doctor.
     */
    @Override
    public String toString() {
        return "[%s][%s, #%s]".formatted( super.toString(), this.specialty, this.npi);
    }
}
