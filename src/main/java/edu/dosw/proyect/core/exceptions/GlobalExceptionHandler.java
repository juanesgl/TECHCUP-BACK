package edu.dosw.proyect.core.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Unified global exception handler.
 * Replaces GlobalErrorHandler and GlobalExceptionHandler — keep only this one.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessRule(BusinessRuleException ex) {
        log.warn("Regla de negocio violada: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Conflicto de regla de negocio", ex.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
        log.warn("Excepción de negocio: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de negocio", ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    @ExceptionHandler(DisponibilidadException.class)
    public ResponseEntity<Map<String, Object>> handleDisponibilidad(DisponibilidadException ex) {
        log.warn("Conflicto de disponibilidad: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Conflicto de disponibilidad", ex.getMessage());
    }

    @ExceptionHandler(TournamentException.class)
    public ResponseEntity<Map<String, Object>> handleTournament(TournamentException ex) {
        log.warn("Error de torneo: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Error de torneo", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        log.warn("Error de validación: {}", errors);
        Map<String, Object> body = buildBody(HttpStatus.BAD_REQUEST, "Error de validación");
        body.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argumento inválido: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, "Argumento inválido", ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        log.warn("Estado inválido: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Conflicto de estado", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        log.error("Error inesperado en el servidor", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ocurrió un error inesperado. Intente de nuevo más tarde.");
    }


    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status,
                                                              String error,
                                                              String message) {
        return ResponseEntity.status(status).body(buildBody(status, error, message));
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error) {
        return ResponseEntity.status(status).body(buildBody(status, error));
    }

    private Map<String, Object> buildBody(HttpStatus status, String error, String message) {
        Map<String, Object> body = buildBody(status, error);
        body.put("message", message);
        return body;
    }

    private Map<String, Object> buildBody(HttpStatus status, String error) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        return body;
    }
}