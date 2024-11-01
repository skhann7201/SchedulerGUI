package com.example.project3;

/**
 * This class represents a user's profile, including first name, last name,
 * and date of birth, and allows comparison between profiles.
 * @author Shahnaz Khan, Vy Nguyen
 *
 */
public class Profile implements Comparable<Profile> {

    // Instance variables
    private String fname;
    private String lname;
    private Date dob;

    /**
     * Constructs a profile with given first name, last name, and date of birth
     *
     * @param fname first name of person being created
     * @param lname last name of person of being created
     * @param dob   date of birth of person being created in the format (MM/DD/YYYY)
     */
    public Profile(String fname, String lname, Date dob) {
        this.fname = fname;
        this.lname = lname;
        this.dob = dob;
    }

    /**
     * Default constructor, that builds no profile.
     * It initiates all instance variables to null.
     */
    public Profile() {
        this.fname = null;
        this.lname= null;
        this.dob = null;
    }

    /**
     * Getter method for first name of the current profile
     *
     * @return first name
     */
    public String getFirstName() {
        return this.fname;
    }

    /**
     * Getter method for last name of the current profile
     *
     * @return last name of current profile
     */
    public String getLastName() {
        return this.lname;
    }

    /**
     * Getter method for date of birth of the current profile
     *
     * @return date of birth of current profile
     */
    public Date getDOB() {
        return this.dob;
    }

    /**
     * Checks if another instance of the object is equal to the current one.
     *
     * @param o the object to compare with
     *
     * @return true if the current object is equal to the object passed, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Profile profile = (Profile) o;
        return fname.equalsIgnoreCase(profile.fname) &&
                lname.equalsIgnoreCase(profile.lname) &&
                dob.equals(profile.dob);
    }

    /**
     * Compares this Profile instance with another Profile instance for order first by last name,
     * then by first name, and finally by date of birth.
     * @param other the Profile instance to compare with
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(Profile other) {

        /** Rewrite this method - Vy
         int result = this.lname.compareTo(other.lname);
         if( result == 0) {
         result = this.fname.compareTo(other.fname);
         if (result == 0) {
         result = this.dob.compareTo(other.dob);
         }
         }
         return result;
         **/

        // Compare by last name
        int lastNameComparison = this.lname.compareTo(other.lname);
        if (lastNameComparison < 0) {
            return -1;
        }else if (lastNameComparison > 0){
            return 1;
        }

        //Compare by first name
        int firstNameComparison = this.fname.compareTo(other.fname);
        if (firstNameComparison < 0) {
            return -1;
        }else if (firstNameComparison > 0){
            return 1;
        }

        // Compare by date of birth
        int dobComparison = this.dob.compareTo(other.dob);
        if (dobComparison < 0){
            return -1;
        }else if (dobComparison > 0) {
            return 1;
        }

        // If all fields are equal, return 0
        return 0;
    }

    /**
     * This method returns a textual representation of Profile object.
     * Format: fname, lname, dob
     * @return String representation of Profile instances.
     */
    @Override
    public String toString() {
        return fname + " " + lname + " " + dob;
    }

    /**
     * This is a testbed main method to test the Profile class functionality.
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args){
        //Test cases for the compareTo() method
        Profile profile1 = new Profile("John","Doe", new Date(12,13,1989));
        Profile profile2 = new Profile("John","Doe", new Date(1,13,1985));
        Profile profile3 = new Profile("Jane","Doe", new Date(5,1,1996));
        Profile profile4 = new Profile("Jane","March", new Date(1,20,2003));
        Profile profile5 = new Profile("Ellington","Duke", new Date(1,20,2003));
        Profile profile6 = new Profile("Ellington","Duke", new Date(1,20,2003));

        System.out.println(profile2.compareTo(profile1)); // Expected -1
        System.out.println(profile3.compareTo(profile2)); // Expected -1
        System.out.println(profile3.compareTo(profile4)); // Expected -1
        System.out.println(profile6.compareTo(profile5)); // Expected 0
        System.out.println(profile1.compareTo(profile2)); // Expected 1
        System.out.println(profile5.compareTo(profile3)); // Expected 1
        System.out.println(profile2.compareTo(profile3)); // Expected 1
    }
}


