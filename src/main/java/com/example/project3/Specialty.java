package com.example.project3;

/**
 * Enum representing different medical specialties and their associated charges.
 * Each specialty has a predefined charge amount.
 * This enum provides a method to retrieve the charge for each specialty
 * and overrides the toString method to return the name of the specialty.
 * @author Shahnaz Khan, Vy Nguyen
 */
public enum Specialty {

    FAMILY(250),
    PEDIATRICIAN(300),
    ALLERGIST(350);

    // Instance variables
    private final int charge;

    /**
     * Constructor that initialzes the charge.
     * @param charge the amount this speciality charges
     */
    private Specialty(int charge){
        this.charge = charge;
    }

    /**
     * This method returns the charge of the specialty.
     * @return the charge of the current instance of Speciality
     */
    public int getCharge(){
        return this.charge;
    }

    /**
     * This toString method overrides
     *
     * @return String output of the instances,
     * the specific specialty with amount of charge for that specialty.
     */
    @Override
    public String toString(){
        return this.name();
    }
}

