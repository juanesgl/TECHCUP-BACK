package edu.dosw.proyect.utils;

public interface AuthStrategy {
    boolean validate(String email);
}
