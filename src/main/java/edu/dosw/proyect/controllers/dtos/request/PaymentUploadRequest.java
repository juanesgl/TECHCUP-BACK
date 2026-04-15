package edu.dosw.proyect.controllers.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Request para subir comprobante de pago.
 * Solo se acepta pago por Nequi según las reglas del torneo.
 * Monto único: $130.000 COP.
 */
@Data
public class PaymentUploadRequest {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer userId;

    @NotNull(message = "El ID del torneo es obligatorio")
    private Integer tournamentId;

    @NotBlank(message = "El nombre del archivo es obligatorio")
    private String fileName;

    @NotBlank(message = "La URL del comprobante es obligatoria")
    private String fileUrl;

    @NotNull(message = "El monto del pago es obligatorio")
    @DecimalMin(value = "130000", message = "El monto debe ser exactamente $130.000")
    @DecimalMax(value = "130000", message = "El monto debe ser exactamente $130.000")
    private BigDecimal monto;

    /**
     * Método de pago — solo se acepta NEQUI según las reglas del torneo.
     */
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
}