package edu.dosw.proyect.controllers.dtos.responses;

import edu.dosw.proyect.controllers.dtos.response.PaymentResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentResponseTest {

    @Test
    void paymentResponse_AllArgsConstructor_ConstruyeCorrectamente() {
        PaymentResponse dto = new PaymentResponse("Pago subido", "PENDING");

        assertEquals("Pago subido", dto.getMessage());
        assertEquals("PENDING", dto.getStatus());
    }

    @Test
    void paymentResponse_Setters_FuncionanCorrectamente() {
        PaymentResponse dto = new PaymentResponse("Pago subido", "PENDING");
        dto.setMessage("Estado actualizado");
        dto.setStatus("APPROVED");

        assertEquals("Estado actualizado", dto.getMessage());
        assertEquals("APPROVED", dto.getStatus());
    }
}
