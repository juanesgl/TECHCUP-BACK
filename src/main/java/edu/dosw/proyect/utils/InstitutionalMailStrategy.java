package edu.dosw.proyect.utils;

public class InstitutionalMailStrategy implements AuthStrategy {
    @Override
    public boolean validate(String email) {
        return email != null && email.endsWith("@mail.escuelaing.edu.co");
    }
}
