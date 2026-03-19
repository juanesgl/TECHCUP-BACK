package edu.dosw.proyect.models;

import java.io.File;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SportProfile {
    private int dorsal;
    private List<String> positions;
    private File photo;
    private int age;
    private Gender gender;
    private boolean available;
    private String preferredPosition;
    private int skillLevel;



    public SportProfile(int dorsal, List<String> positions, File photo, int age, Gender gender, boolean available) {
        this.dorsal = dorsal;
        this.positions = positions;
        this.photo = photo;
        this.age = age;
        this.gender = gender;
        this.available = available;
    }

    public SportProfile(String preferredPosition, int skillLevel) {
        this.preferredPosition = preferredPosition;
        this.skillLevel = skillLevel;
    }

    public void saveProfile() {
        System.out.println("Perfil deportivo guardado exitosamente.");
    }

    public void toggleAvailability() {
        this.available = !this.available;
    }


}
