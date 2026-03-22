package edu.dosw.proyect.models;

public interface User {
    Long getId();
    String getName();
    String getEmail();
    String getPassword();
    String getRole();
    void setId(Long id);
    void setName(String name);
    void setEmail(String email);
    void setPassword(String password);
    void setRole(String role);
    String getProgramaAcademico();
    void setProgramaAcademico(String programaAcademico);
}
