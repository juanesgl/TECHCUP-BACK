package edu.dosw.proyect.models;

public class FamilyMember extends AbstractUser {
    private SportProfile sportProfile;

    public FamilyMember(String name, String email, String password, SportProfile sportProfile) {
        super(name, email, password, "FAMILY_MEMBER");
        this.sportProfile = sportProfile;
    }

    public SportProfile getSportProfile() { return sportProfile; }
    public void setSportProfile(SportProfile sportProfile) { this.sportProfile = sportProfile; }
}
