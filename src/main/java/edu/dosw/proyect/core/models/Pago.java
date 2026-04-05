package edu.dosw.proyect.core.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    private Long id;
    private Equipo equipo;
    private Tournament torneo;
    private String comprobanteUrl;
    private String estado;
    private LocalDateTime fechaSubida;
    private LocalDateTime fechaRevision;
    private User revisadoPor;
}

