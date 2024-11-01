package com.example.project3;

/**
 * The Provider class represents a healthcare provider in the clinic management system.
 * This class extends the Person class, inheriting its properties and methods.
 *  This class is an abstract class and defines a Location for Practice
 *  an abstract method rate()
 *  Subclasses like doctor and Technician will implement the rate() method.
 *
 * @author Vy Nguyen
 */
public abstract class Provider extends Person {
    private Location location;

    /**
     * Default constructor
     */
    public Provider(){
        super();
        this.location = null;
    }

    /** Constructor to create a Provider instance.
     * @param profile the profile of the provider.
     * @param location the location of the provider's practice.
     */
    public Provider(Profile profile, Location location) {
        super(profile);
        this.location = location;
    }

    /**
     * Returns the location of the provider.
     * @return the provider's location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Abstract method to get the charging rate for the provider.
     * @return the charging rate per visit for seeing patients.
     */
    public abstract int rate();

    /**
     * Overrides the toString method to include provider details.
     * @return a string representation of the provider's profile and location.
     */
    @Override
    public String toString() {
        return super.toString() + ", " + location.toString();
    }
}
