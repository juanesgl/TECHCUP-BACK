package edu.dosw.proyect.utils;

public class GmailStrategy implements AuthStrategy {
    @Override
    public boolean validate(String email) {
        return email != null && email.endsWith("@gmail.com");
    }
}
