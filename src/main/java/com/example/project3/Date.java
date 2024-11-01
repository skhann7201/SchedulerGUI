package com.example.project3;

import java.util.Calendar;
/**
 * This class represents a Date with day, month, and year.
 * It implements methods to validate the date and compare dates and
 * provide a string presentation of the date.
 *
 * @author Vy Nguyen, Shahnaz Khan
 */
public class Date implements Comparable<Date> {

    // Constants for leap year calculation
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUADRICENTENNIAL = 400;
    public static final int DAYS_IN_FEB_LEAP = 29;

    // Instance variables for day, month, and year
    private int year;
    private int month;
    private int day;

    /**
     * Default constructor for Date class.
     * Initializes the date to January 1st, 1900.
     */
    public Date() {
    }

    /**
     * Constructor to create a Date instance.
     *
     * @param day        the day of the date
     * @param month     the month of the date
     * @param year      the year of the date
     */
    public Date(int month, int day, int year) {
        this.month = month - 1 ; // Adjust to zero-based calendar
        this.day = day;
        this.year = year;
    }

    /**
     * This constructor takes in a string input in the format MM/DD/YYYY,
     * or input in the format YYYY-MM-DD
     * splits and parses the input to initialize instance variables.
     *
     * @param date the string representing the date (MM/DD/YYYY)
     */
    public Date(String date) {
        String[] parts;
        if (date.contains("/")){
            parts = date.split("/");
            if (parts.length == 3) {
                // Trim spaces around each part and check if they are numeric
                this.month = Integer.parseInt(parts[0].trim()) - 1; //convert to zero based calendar
                this.day = Integer.parseInt(parts[1].trim());
                this.year = Integer.parseInt(parts[2].trim());
            }
        } else if (date.contains("-")){
            parts = date.split("-");
            if (parts.length == 3) {
                this.year = Integer.parseInt(parts[0].trim());
                this.month = Integer.parseInt(parts[1].trim()) - 1; // Convert to zero-based calendar
                this.day = Integer.parseInt(parts[2].trim());
            }
        }
    }



    /**
     * Helper method: Convert this Date object to a Calendar object.
     *
     *  @return a Calendar object representing this Date object.
     */
    public Calendar toCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, this.year);
        calendar.set(Calendar.MONTH, this.month);
        calendar.set(Calendar.DAY_OF_MONTH, this.day);
        return calendar;
    }

    /**
     * Helper method to check if a given year is a leap year.
     *
     * @param year the year to check
     * @return true if the year is a leap year, false otherwise
     */
    private boolean isLeapYear(int year) {
        if (year % QUADRENNIAL == 0) {
            if (year % CENTENNIAL == 0) {
                return year % QUADRICENTENNIAL == 0 ;
            }
            return true;
        }
        return false;
    }

    /**
     * Helper method to get the number of days in a given month and year.
     *
     * @param month the month to check
     * @param year  the year to check
     * @return the number of days in the specified month and year
     */
    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.FEBRUARY:
                return isLeapYear(year) ? DAYS_IN_FEB_LEAP : 28;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            default: return 31;
        }
    }

    /**
     * Validates if the current date instance is a valid calendar date.
     *
     * @return true if the date is valid, false otherwise
     */
    public boolean isValid() {
        // Check for invalid year
        if ( year < 1900){
            return false;
        }

        // Check for invalid month
        if ( month < Calendar.JANUARY || month > Calendar.DECEMBER) {
            return false;
        }

        // Check for invalid day
        int maxDay = getDaysInMonth(month, year);
        return day >=1 && day <= maxDay;
    }

    /**
     * Check if this Date represents today's date by comparing it to the current Calendar system.
     *
     * @return true if the date is today, false otherwise.
     */
    public boolean isToday(){
        Calendar today = Calendar.getInstance();
        return this.year == today.get(Calendar.YEAR)
                && this.month == today.get(Calendar.MONTH)
                && this.day == today.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * Checks if this Date represent the date before today.
     * The method compares the year, month, and day of the current date object with today's date.
     *
     * @return true if this Date is before today, false otherwise.
     */
    public boolean isPastDate() {
        Calendar today = Calendar.getInstance();

        return this.year < today.get(Calendar.YEAR) ||
                (this.year == today.get(Calendar.YEAR) && this.month < today.get(Calendar.MONTH)) ||
                (this.month == (today.get(Calendar.MONTH)) && this.day < today.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Checks if this Date represents a date after today's date.
     * The method compares the year, month, and day of the current date object with today's date.
     *
     * @return true if this Date is after today, false otherwise.
     */
    public boolean isFutureDate() {
        Calendar today = Calendar.getInstance();

        return this.year > today.get(Calendar.YEAR) ||
                (this.year == today.get(Calendar.YEAR) && this.month > today.get(Calendar.MONTH)) ||
                (this.month == (today.get(Calendar.MONTH)) && this.day > today.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Checks if this Date is on a Weekend ( Saturday or Sunday).
     *
     * @return true if the date is a weekend, false otherwise.
     */
    public boolean isWeekend() {
        Calendar calendar = this.toCalendar();

        calendar.set(Calendar.YEAR, this.year);
        calendar.set(Calendar.MONTH, this.month);
        calendar.set(Calendar.DAY_OF_MONTH, this.day);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    /**
     *  Checks if this Date is more than six months after today's date.
     *
     * @return true if this date is more than six months after today's date.
     */
    public boolean isWithinSixMonths() {
        Calendar sixMonths = Calendar.getInstance();
        sixMonths.add(Calendar.MONTH, 6);

        return (this.year > sixMonths.get(Calendar.YEAR)) ||
                (this.year == sixMonths.get(Calendar.YEAR) && this.month > sixMonths.get(Calendar.MONTH)) ||
                (this.year == sixMonths.get(Calendar.YEAR) && this.month == sixMonths.get(Calendar.MONTH)
                        && this.day >= sixMonths.get(Calendar.DAY_OF_MONTH));
    }


    /**
     * Overrides the equals method to compare two Date objects.
     *
     * @param obj the object to compare to
     * @return true if the two Date objects represent the same date, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Date) {
            Date date = (Date) obj;
            return this.day == date.day
                    && this.month == date.month
                    && this.year == date.year;
        }
        return false;
    }

    /**
     * Overrides the compareTo method from the Comparable interface.
     * Compares this Date object with another Date object.
     *
     * @param other the Date object to compare to
     * @return a negative integer, zero, or a positive integer as this date is
     *         less than, equal to, or greater than the specified date.
     */
    @Override
    public int compareTo(Date other) {
        if (this.year != other.year) {
            return Integer.compare(this.year, other.year);
        }
        if(this.month != other.month) {
            return Integer.compare(this.month, other.month);
        }
        return Integer.compare(this.day, other.day);
    }

    /**
     * Overrides the toString method to provide a formatted string representation
     * of the Date object in the format mm/dd/yyyy.
     *
     * @return a string representation of the Date in the format mm/dd/yyyy
     */
    @Override
    public String toString() {
        return String.format("%d/%d/%d", this.month + 1, this.day, this.year);
    }

}



