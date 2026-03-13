package edu.dosw.proyect.models;

import java.io.File;
import java.util.List;

public class SportProfile {
    private int dorsal;
    private List<String> positions;
    private File photo;
    private int age;
    private Gender gender;
    private boolean available;

    public SportProfile() {}

    public SportProfile(int dorsal, List<String> positions, File photo, int age, Gender gender, boolean available) {
        this.dorsal = dorsal;
        this.positions = positions;
        this.photo = photo;
        this.age = age;
        this.gender = gender;
        this.available = available;
    }

    public void saveProfile() {
        System.out.println("Perfil deportivo guardado exitosamente.");
    }

    public void toggleAvailability() {
        this.available = !this.available;
    }

    public int getDorsal() { return dorsal; }
    public void setDorsal(int dorsal) { this.dorsal = dorsal; }
    public List<String> getPositions() { return positions; }
    public void setPositions(List<String> positions) { this.positions = positions; }
    public File getPhoto() { return photo; }
    public void setPhoto(File photo) { this.photo = photo; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
public class SportProfile {
    private String preferredPosition;
    private int skillLevel;

    public SportProfile() {}

    public SportProfile(String preferredPosition, int skillLevel) {
        this.preferredPosition = preferredPosition;
        this.skillLevel = skillLevel;
    }

    public String getPreferredPosition() { return preferredPosition; }
    public void setPreferredPosition(String preferredPosition) { this.preferredPosition = preferredPosition; }

    public int getSkillLevel() { return skillLevel; }
    public void setSkillLevel(int skillLevel) { this.skillLevel = skillLevel; }
}
