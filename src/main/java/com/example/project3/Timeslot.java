package com.example.project3;

/**
 * The Timeslot class represents a time slot in a scheduling system,
 * allowing for the creation of time slots corresponding to specific
 * hours and minutes in a 12-slot format, spanning from 9:00 AM to 4:30 PM.
 * The class implements Comparable to allow sorting of timeslots based on
 * their time.
 *
 * @author Shahnaz Khan, Vy Nguyen
 */
public class Timeslot implements Comparable<Timeslot> {
    private final int hour;
    private final int minute;
    private static final int MIN_SLOT = 1;
    private static final int MAX_SLOT = 12;
    private static final int MORNING_BASE_HOUR = 9; // 9 AM
    private static final int AFTERNOON_START_HOUR = 14; // 2 PM

    /**
     * Default Constructor
     */
    public Timeslot() {
        this.hour = 0;
        this.minute = 0;
    }

    /**
     * Constructs a Timeslot from a slot number string.
     * @param slotNumberStr The string representation of the slot number.
     */
    public Timeslot(String slotNumberStr) {
        int slotNumber = parseSlotNumber(slotNumberStr);
        if (!isValidSlotNumber(slotNumber)) {
            this.hour = MORNING_BASE_HOUR;
            this.minute = 30; // Default to 9:30 AM if invalid
            return;
        }
        this.hour = calculateHour(slotNumber);
        this.minute = calculateMinute(slotNumber);
    }


    /**
     * Constructs a Timeslot with specified hour and minute.
     * @param hour The hour of the timeslot (0-23).
     * @param minute The minute of the timeslot (0-59).
     * @throws IllegalArgumentException if the hour or minute is invalid.
     */
    public Timeslot(int hour, int minute) {
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new IllegalArgumentException("Invalid hour or minute.");
        }
        this.hour = hour;
        this.minute = minute;
    }

    // Parse slot number from string
    private int parseSlotNumber(String slotNumberStr) {
        try {
            return Integer.parseInt(slotNumberStr);
        } catch (NumberFormatException e) {
            throw new NumberFormatException(slotNumberStr + " is not a valid time slot.");
        }
    }

    /**
     * Checks if the slot number is valid (between MIN_SLOT and MAX_SLOT).
     * @param slotNumber The slot number to check.
     * @return true if valid, false otherwise.
     */
    public boolean isValidSlotNumber(int slotNumber) {
        return slotNumber >= MIN_SLOT && slotNumber <= MAX_SLOT;
    }

    // Calculate hour from slot number
    private int calculateHour(int slotNumber) {
        if (slotNumber <= 6) {
            return MORNING_BASE_HOUR + (slotNumber - 1) / 2; // Morning slots
        } else {
            return AFTERNOON_START_HOUR + (slotNumber - 7) / 2; // Afternoon slots
        }
    }

    // Calculate minute from slot number
    private int calculateMinute(int slotNumber) {
        return (slotNumber % 2 == 1) ? 0 : 30; // Odd slots = 00 min, even slots = 30 min
    }

    /**
     * Gets the slot number based on hour and minute.
     * @return The slot number, or -1 if invalid.
     */
    public int getSlotNumber() {
        if (hour >= MORNING_BASE_HOUR && hour < 12) {
            int slot = (hour - MORNING_BASE_HOUR) * 2 + 1; // Convert hour to slot
            if (minute == 30) {
                slot++;
            }
            return slot;
        } else if (hour >= AFTERNOON_START_HOUR && hour <= 16) {
            return 7 + (hour - AFTERNOON_START_HOUR) * 2 + (minute == 30 ? 1 : 0);
        } else {
            return -1; // Indicates an invalid timeslot
        }
    }

    /**
     * Checks if the timeslot is valid (matches the defined hours and minutes).
     * @return true if the timeslot is valid, false otherwise.
     */
    public boolean isValidTimeslot() {
        return (hour == 9 || hour == 10 || hour == 11 ||
                hour == 14 || hour == 15 || hour == 16) &&
                (minute == 0 || minute == 30);
    }

    /**
     * Returns the hour of current instance
     * @return the hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Return the minute of current instance
     * @return the minute of current instance
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Compares this Timeslot with another Timeslot.
     * @param other The other Timeslot to compare to.
     * @return A negative integer, zero, or a positive integer as this
     *         Timeslot is less than, equal to, or greater than the specified Timeslot.
     */
    @Override
    public int compareTo(Timeslot other) {
        if (this.hour != other.hour) {
            return Integer.compare(this.hour, other.hour);
        }
        return Integer.compare(this.minute, other.minute);
    }

    /**
     * Checks if this Timeslot is equal to another object.
     * @param obj The object to compare to.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Timeslot) {
            Timeslot other = (Timeslot) obj;
            return this.hour == other.hour && this.minute == other.minute;
        }
        return false;
    }

    /**
     * Returns the time of the Timeslot in "H:MM" format.
     * @return The formatted string representation of the Timeslot.
     */
    public String toTimeFormat() {
        String period = (this.hour < 12) ? "AM" : "PM";
        int displayHour = (this.hour % 12 == 0) ? 12 : this.hour % 12;

        return String.format("%d:%02d %s",displayHour, minute, period);
    }

    /**
     * Returns a string representation of the Timeslot.
     * @return The string representation of the slot number.
     */
    @Override
    public String toString() {
        return Integer.toString(getSlotNumber());
    }
}
