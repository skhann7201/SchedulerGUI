package com.example.project3;

/**
 * This class represents a patient in the clinic management system.
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Patient extends Person {
    // Instance variable
    private Visit visit; // linked list of visits

    /**
     * Default constructor that initializes a Patient with no visits.
     */
    public Patient() {
        super();
        this.visit = null;
    }

    /**
     * Constructs a new Patient object with the given profile and visit history.
     *
     * @param profile the profile of the person
     * @param visit the visit history of the patient.
     */
    public Patient(Profile profile, Visit visit) {
        super(profile);
        this.visit = visit;
    }

    /**
     * Get the visit history of the patient.
     *
     * @return the visit history of the patient
     */
    public Visit getVisit() {
        return visit;
    }

    /**
     * Set the visit history of the patient.
     *
     * @param visit visit history of the patient
     */
    public void setVisit(Visit visit) {
        this.visit = visit;
    }
}

