package it.sara.demo.exception;

import it.sara.demo.dto.StatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class CentralExceptionHandler {

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<StatusDTO> handleGeneric(GenericException ex) {
        StatusDTO status = ex.getStatus();
        if (status.getTraceId() == null || status.getTraceId().isBlank()) {
            status.setTraceId(UUID.randomUUID().toString());
        }
        return ResponseEntity.ok(status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StatusDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        StatusDTO status = new StatusDTO("Invalid credentials");
        status.setCode(400);
        status.setMessage("Malformed JSON: " + rootMessage(ex));
        status.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.ok(status);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<StatusDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        StatusDTO status = new StatusDTO("Invalid credentials");
        status.setCode(405);
        status.setMessage("Method not supported: " + ex.getMethod());
        status.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.ok(status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StatusDTO> handleUnexpected(Exception ex) {
        StatusDTO status = new StatusDTO("Invalid credentials");
        status.setCode(500);
        status.setMessage("Unexpected error: " + rootMessage(ex));
        status.setTraceId(UUID.randomUUID().toString());
        return ResponseEntity.ok(status);
    }

    private String rootMessage(Throwable ex) {
        Throwable root = ex;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root.getMessage();
    }

}
