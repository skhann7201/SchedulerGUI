package com.example.project3;

/**
 * Enum representing the locations where providers operate.
 * Each location has a city, county, and zip code.
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public enum Location{

    BRIDGEWATER("Somerset", "08807"),
    EDISON("Middlesex", "08817"),
    PISCATAWAY("Middlesex", "08854"),
    PRINCETON("Mercer", "08542"),
    MORRISTOWN("Morris", "07960"),
    CLARK("Union", "07066");

    // Instance variables
    private final String county;
    private final String zip;

    /**
     * Constructor for the Location enum.
     *
     * @param county    the county of the location
     * @param zip       the zip code of the location
     */
    Location(String county, String zip) {
        this.county = county;
        this.zip = zip;
    }

    /**
     * Getter for the county
     *
     * @return the county of the location
     */
    public String getCounty() {
        return county;
    }

    /**
     * Getter for the zip code.
     *
     * @return the zip code of the location
     */
    public String getZip() {
        return zip;
    }

    /**
     * Overrides the toString method to provide a formatted string representation of the location.
     * Format: CITY, COUNTY, ZIP
     *
     * @return a string representation of the location
     */
    @Override
    public String toString() {
        return this.name() + ", " + county + " " + zip;
    }
}

