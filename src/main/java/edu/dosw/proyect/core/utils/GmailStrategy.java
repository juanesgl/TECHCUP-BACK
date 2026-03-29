package edu.dosw.proyect.core.utils;

public class GmailStrategy implements AuthStrategy {
    @Override
    public boolean validate(String email) {
        return email != null && email.endsWith("@gmail.com");
    }
}

