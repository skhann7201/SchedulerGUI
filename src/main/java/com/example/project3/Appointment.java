package com.example.project3;
/**
 * This class represents an appointment with a specific date, timeslot, patient, and provider.
 * It provides methods to compare, check equality, and convert the appointment to a string.
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Appointment implements Comparable<Appointment>{

    // Instance variables
    private Date date;
    protected Timeslot timeslot;
    protected Person patient;
    protected Person provider;

    /**
     * Default constructor for Appointment class.
     * Initializes all fields to null or default values.
     */
    public Appointment() {
        this.date = null;
        this.timeslot = null;
        this.patient = null;
        this.provider = null;
    }

    /**
     * This constructor creates an Appointment instance with date, timeslot and patient.
     *
     * @param date the date of the appointment
     * @param timeslot the timeslot of the appointment
     * @param patient   the patient involved with this appointment
     */
    public Appointment(Date date, Timeslot timeslot, Person patient){
        this.date = date;
        this.timeslot= timeslot;
        this.patient = patient;
    }

    /**
     * Constructor to create an Appointment instance with date, timeslot, patient, and provider.
     *
     * @param date      the date of the appointment
     * @param timeslot  the timeslot of the appointment
     * @param patient   the patient involved in the appointment
     * @param provider  the provider handling the appointment
     */
    public Appointment(Date date, Timeslot timeslot, Person patient, Person provider) {
        this.date = date;
        this.timeslot = timeslot;
        this.patient = patient;
        this.provider = provider;
    }

    /**
     * This method returns the profile of the current instance of Appointment
     * @return Profile of current Appointment
     */
    public Person getProfile(){
        return this.patient;
    }

    /**
     * This method returns the provider of the current instance of Appointment
     * @return the Provider of current Appointment
     */
    public Person getProvider(){
        return this.provider;
    }

    /**
     * Sets the current provider to the parameter provider passed in
     * @param provider provider to set the instance to
     */
    public void setProvider(Person provider){
        this.provider = provider;
    }

    /**
     * This method returns the Date of the current instance of Appointment
     * @return the date of the current Appointment
     */
    public Date getDate(){
        return this.date;
    }

    /**
     * This method returns the Timeslot of the current instance of Appointment
     * @return the Timeslot of current Appointment
     */
    public Timeslot getTimeslot(){
        return this.timeslot;
    }

    /**
     * Overrides the compareTo method from the Comparable interface.
     * Compares this appointment with another appointment based on the date and timeslot.
     *
     * @param other the object to compare to
     * @return a negative integer, zero, or a positive integer as this appointment is
     *          earlier than, equal to, or later than the specified appointment.
     */
    @Override
    public int compareTo(Appointment other) {
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return this.timeslot.compareTo(other.timeslot);
    }


    /**
     * Overrides the equals method to compare two Appointment objects.
     * Appointments are considered equal if they have the same date, timeslot, and patient.
     *
     * @param obj the object to compare to
     * @return true if the two appointments are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Appointment) {
            Appointment other = (Appointment) obj;
            return this.date.equals(other.date) &&
                    this.timeslot.equals(other.timeslot) &&
                    this.patient.equals(other.patient);

        }
        return false;
    }

    /**
     * Overrides the toString method to provide a formatted string representation of the appointment.
     *
     * @return a string representation of the appointment
     */
    @Override
    public String toString() {
        return String.format("%s %s %s %s %s %s",
                date.toString(),
                timeslot.toTimeFormat(),
                patient.getProfile().getFirstName(),
                patient.getProfile().getLastName(),
                patient.getProfile().getDOB(),
                provider.toString());
    }
}