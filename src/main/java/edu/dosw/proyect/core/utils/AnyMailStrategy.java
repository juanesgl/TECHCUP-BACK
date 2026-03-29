package edu.dosw.proyect.core.utils;

public class AnyMailStrategy implements AuthStrategy {
    @Override
    public boolean validate(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}

