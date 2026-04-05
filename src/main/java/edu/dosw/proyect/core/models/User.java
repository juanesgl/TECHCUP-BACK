package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Column(name = "apellido")
    private String lastName;

    @Column(name = "correo", unique = true, nullable = false)
    private String email;

    @Column(name = "contrasena_hash", nullable = false)
    private String password;

    @Column(name = "tipo_usuario", nullable = false)
    private String role;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "activo")
    @Builder.Default
    private boolean active = true;

    @Column(name = "programa_academico")
    private String academicProgram;

    @PrePersist
    protected void onCreate() {
        if (registrationDate == null) {
            registrationDate = LocalDateTime.now();
        }
        if (!active) {
            active = true;
        }
    }
}