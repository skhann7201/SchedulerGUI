package com.example.project3.models;

import com.example.project3.Date;
import org.junit.Test;
import static org.junit.Assert.*;
public class DateTest {

    @Test
    public void testIsValid() {
        // Invalid Case 1: Year before 1900
        Date date1 = new Date(2,2, 800);
        assertFalse("Year is before 1900", date1.isValid());

        // Invalid case 2: invalid date of february for non-Leap year
        Date date2 = new Date(2, 29, 2003);
        assertFalse("Days in February is invalid for non-leap year", date2.isValid());

        // Invalid Case 3: invalid month, valid day, leap year
        Date date3 = new Date(13,15, 2020);
        assertFalse("Month is invalid", date3.isValid());

        // Invalid Case 4: valid month, invalid day, leap year
        Date date4 = new Date(4, 31, 2020);
        assertFalse("Day is invalid", date4.isValid());

        // Valid Case 1: valid day, valid month, leap year
        Date date5 = new Date(2,29, 2020);
        assertTrue("Date is valid for leap year", date5.isValid());

        // Valid Case 2: valid day, valid month, non-leap year
        Date date6 = new Date(2,28, 2003);
        assertTrue("Date is valid for non-leap year", date6.isValid());
    }

    @Test
    public void testIsWeekend(){
        Date date = new Date("10/19/2024");
        assertTrue("Date is a weekend", date.isWeekend());

        Date date2 = new Date(10,15,2024);
        assertFalse("Date is not a weekend", date2.isWeekend());
    }

    @Test
    public void testIsWithinSixMonths() {
        Date date = new Date("10/19/2025");
        assertTrue("Date is six months from today", date.isWithinSixMonths());

        Date date2 = new Date("11/15/2024");
        assertFalse("Date is not six months from today", date2.isWithinSixMonths());
    }

    @Test
    public void testIsFutureDate() {
        Date date = new Date ("10/19/2025");
        assertTrue("Date is future", date.isFutureDate());

        Date date2 = new Date("10/14/2024");
        assertFalse("Date is not future", date2.isFutureDate());
    }

    @Test
    public void testIsPastDate() {
        Date date = new Date("10/14/2024");
        assertTrue("Date is past", date.isPastDate());

        Date date2 = new Date("10/14/2023");
        assertFalse("Date is not past", date2.isPastDate());
    }


    @Test
    public void testEquals() {
        Date dateStr = new Date("10/19/2024");
        Date date = new Date(10,19, 2024);
        assertEquals("The date should be equal", dateStr, date);
    }
}