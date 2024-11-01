package com.example.project3.models;

import com.example.project3.Date;
import com.example.project3.Profile;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ProfileTest {
    private Profile profile1, profile2, profile3, profile4, profile5, profile6;

    @Before
    public void setUp() throws Exception {
        profile1 = new Profile("John","Doe", new Date(12,13,1989));
        profile2 = new Profile("John","Doe", new Date(1,13,1985));
        profile3 = new Profile("Jane","Doe", new Date(5,1,1996));
        profile4 = new Profile("Jane","March", new Date(1,20,2003));
        profile5 = new Profile("Ellington","Duke", new Date(1,20,2003));
        profile6 = new Profile("Ellington","Duke", new Date(1,20,2003));
    }

    @Test
    public void testCompareTo() {
        assertEquals(-1, profile2.compareTo(profile1) );
        assertEquals(-1, profile3.compareTo(profile2) );
        assertEquals(-1, profile3.compareTo(profile4) );
        assertEquals(0, profile6.compareTo(profile5) );
        assertEquals(1, profile1.compareTo(profile2) );
        assertEquals(1, profile5.compareTo(profile3) );
        assertEquals(1, profile2.compareTo(profile3) );
        assertEquals(1, profile1.compareTo(profile3) );
    }

}