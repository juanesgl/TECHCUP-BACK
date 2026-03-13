package edu.dosw.proyect.models;

public abstract class AbstractUser implements User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public AbstractUser() {}

    public AbstractUser(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override public Long getId() { return id; }
    @Override public void setId(Long id) { this.id = id; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }
    @Override public String getEmail() { return email; }
    @Override public void setEmail(String email) { this.email = email; }
    @Override public String getPassword() { return password; }
    @Override public void setPassword(String password) { this.password = password; }
    @Override public String getRole() { return role; }
    @Override public void setRole(String role) { this.role = role; }
}
