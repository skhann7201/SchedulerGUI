package com.example.project3;

/**
 * This class represents a Visit, which contains an Appointment
 * and a link to the next Visit.
 * The Visit class is used to form a linked list of visits for a patient.
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Visit {

    // Instance variables
    private Appointment appointment; // hold appointment details
    private Visit next; // points to the next visit in the linked list

    /**
     * Constructor to create a Visit instance.
     *
     * @param appointment   the appointment associated with this visit
     * @param next          the next visit in the linked list.
     */
    public Visit(Appointment appointment, Visit next) {
        this.appointment = appointment;
        this.next = next;
    }

    /**
     * Getter for the appointment associated with this visit.
     *
     * @return the appointment for this visit.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    /**
     * Getter for the next visit in the linked list.
     *
     * @return the next visit.
     */
    public Visit getVisit() {
        return next;
    }

    /**
     * Setter for the appointment associated with this visit.
     *
     * @param appointment the appointment to set for this visit
     */
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    /**
     * Setter for the next visit in the linked list
     *
     * @param next the next visit to be set.
     */
    public void setNextVisit(Visit next) {
        this.next = next;
    }

    /**
     * Overrides the toString method to provide a string representation of the visit.
     * The string format is provided by the toString() method of the associated Appointment object.
     * @return a string representation of the visit.
     */
    @Override
    public String toString() {
        return "Visit:\n" +
                "Appointment:" + appointment + "\n" +
                "Next Visit: " + (next != null ? next.getAppointment() : "No next visit") + "\n";
    }
}
