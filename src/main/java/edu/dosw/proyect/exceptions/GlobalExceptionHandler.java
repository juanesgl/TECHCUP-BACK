package edu.dosw.proyect.exceptions;

<<<<<<< HEAD
import edu.dosw.proyect.dtos.PaymentResponse;
=======
>>>>>>> feature/BuscarFiltroJugadores
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

<<<<<<< HEAD
=======
import java.util.HashMap;
import java.util.Map;

>>>>>>> feature/BuscarFiltroJugadores
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
<<<<<<< HEAD
    public ResponseEntity<PaymentResponse> handleBusinessException(BusinessException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // 400
                .status(HttpStatus.BAD_REQUEST)
                .body(new PaymentResponse(ex.getMessage(), "ERROR"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<PaymentResponse> handleGenericException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR) // 500
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new PaymentResponse("Error interno del servidor", "ERROR"));
    }
}
=======
    public ResponseEntity<Map<String, String>> handleBusinessException(BusinessException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", "ERROR");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Error interno del servidor");
        error.put("status", "ERROR");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
>>>>>>> feature/BuscarFiltroJugadores
