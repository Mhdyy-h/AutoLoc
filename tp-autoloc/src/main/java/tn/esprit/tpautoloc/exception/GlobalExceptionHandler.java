package tn.esprit.tpautoloc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String KEY_STATUT = "statut";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_HORODATAGE = "horodatage";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                erreurs.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(erreurs);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_STATUT, HttpStatus.NOT_FOUND.value());
        body.put(KEY_MESSAGE, ex.getMessage());
        body.put(KEY_HORODATAGE, LocalDateTime.now(ZoneId.systemDefault()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_STATUT, HttpStatus.BAD_REQUEST.value());
        body.put(KEY_MESSAGE, ex.getMessage());
        body.put(KEY_HORODATAGE, LocalDateTime.now(ZoneId.systemDefault()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(KEY_STATUT, HttpStatus.BAD_REQUEST.value());
        body.put(KEY_MESSAGE, ex.getMessage());
        body.put(KEY_HORODATAGE, LocalDateTime.now(ZoneId.systemDefault()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}
