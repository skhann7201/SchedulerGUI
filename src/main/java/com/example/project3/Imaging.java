package com.example.project3;

/**
 * This class extends the Appointment class to hold additional data for imaging appointments.
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Imaging extends Appointment {
    private Radiology room;

    /**
     * Constructs an Imaging appointment by initializing it with the specified date, timeslot,
     * patient, and Radiology room.
     *
     * @param date      the date of the imaging appointment
     * @param timeslot  the timeslot of the imaging appointment
     * @param patient   the patient scheduled for the imaging appointment
     * @param room      the Radiology room where the imaging appointment will take place
     */
    public Imaging(Date date, Timeslot timeslot, Patient patient , Radiology room) {
        super(date, timeslot, patient, null);
        this.room = room;
    }

    /**
     * Returns the radiology room where the imaging appointment will take place.
     * @return the Radiology room associated with this imaging appointment
     */
    public Radiology getRoom() {
        return room;
    }

    /**
     * Sets the Radiology room for this imaging appointment.
     *
     * @param room the Radiology room to be set
     */
    public void setRoom(Radiology room) {
        this.room = room;
    }

    /**
     * Provides a string representation of the Imaging appointment,
     * including details from the superclass Appointment and the Radiology room.
     *
     * @return a string representation of the Imaging appointment
     */
    @Override
    public String toString() {
        return super.toString() + "[" + room + "]";
    }
}

