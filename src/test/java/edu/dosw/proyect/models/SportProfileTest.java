package edu.dosw.proyect.models;

import edu.dosw.proyect.core.models.SportProfile;
import edu.dosw.proyect.core.models.enums.Gender;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SportProfileTest {

    @Test
    void testSportProfileConstructorAndGetters() {
        List<String> positions = Arrays.asList("Delantero", "Medio");
        File photo = new File("photo.path");
        SportProfile profile = new SportProfile(10, positions, photo, 25, Gender.MASCULINO, true);

        assertEquals(10, profile.getDorsal());
        assertEquals(positions, profile.getPositions());
        assertEquals(photo, profile.getPhoto());
        assertEquals(25, profile.getAge());
        assertEquals(Gender.MASCULINO, profile.getGender());
        assertTrue(profile.isAvailable());
    }

    @Test
    void testSportProfileSetters() {
        SportProfile profile = new SportProfile();
        List<String> positions = Arrays.asList("Portero");
        File photo = new File("test.jpg");

        profile.setDorsal(1);
        profile.setPositions(positions);
        profile.setPhoto(photo);
        profile.setAge(30);
        profile.setGender(Gender.FEMENINO);
        profile.setAvailable(false);

        assertEquals(1, profile.getDorsal());
        assertEquals(positions, profile.getPositions());
        assertEquals(photo, profile.getPhoto());
        assertEquals(30, profile.getAge());
        assertEquals(Gender.FEMENINO, profile.getGender());
        assertFalse(profile.isAvailable());
    }

    @Test
    void testToggleAvailability() {
        SportProfile profile = new SportProfile();
        profile.setAvailable(true);

        profile.toggleAvailability();
        assertFalse(profile.isAvailable());

        profile.toggleAvailability();
        assertTrue(profile.isAvailable());
    }

    @Test
    void testSaveProfile() {
        SportProfile profile = new SportProfile();
        assertDoesNotThrow(() -> profile.saveProfile());
    }

    @Test
    void testGenderEnum() {
        assertEquals("MASCULINO", Gender.MASCULINO.name());
        assertEquals("FEMENINO", Gender.FEMENINO.name());
        assertEquals("OTRO", Gender.OTRO.name());
        
        Gender[] genders = Gender.values();
        assertEquals(3, genders.length);
        assertEquals(Gender.MASCULINO, Gender.valueOf("MASCULINO"));
    }
}

