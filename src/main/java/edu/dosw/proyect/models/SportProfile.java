package edu.dosw.proyect.models;

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
