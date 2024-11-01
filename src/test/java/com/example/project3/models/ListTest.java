package com.example.project3.models;

import com.example.project3.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ListTest {
    private List<Provider> providerList;
    private Doctor doctor1;
    private Technician technician1;

    @Before
    public void setUp() {
        providerList = new List<>();
        doctor1 = new Doctor(new Profile("RACHEL", "LIM", new Date("11/30/1975")),
                Location.BRIDGEWATER, Specialty.PEDIATRICIAN, "23");
        technician1 = new Technician(new Profile("JENNY", "PATEL", new Date("8/9/1991")),
                Location.BRIDGEWATER, 125);
    }

    @Test
    public void testAdd() {
        // Before adding
        assertEquals("Initial size should be 0", 0, providerList.size());

        // Add doctor and check size
        providerList.add(doctor1);
        assertEquals("Size should be 1 after adding one doctor",1, providerList.size());

        // Add technician and check size
        providerList.add(technician1);
        assertEquals("Size should be 2 after adding one technician",2, providerList.size());
    }

    @Test
    public void testRemove() {

        // add doctor and technician to provider list
        providerList.add(doctor1);
        providerList.add(technician1);

        // Remove doctor and check size
        providerList.remove(doctor1);
        assertEquals("Size should be 1 after removing one doctor", 1, providerList.size());

        // Remove technician and check size
        providerList.remove(technician1);
        assertEquals("Size should be 0 after removing one technician", 0, providerList.size());
    }
}