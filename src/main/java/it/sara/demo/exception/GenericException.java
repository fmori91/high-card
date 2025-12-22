package it.sara.demo.exception;

import it.sara.demo.dto.StatusDTO;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GenericException extends Exception {

    public final static StatusDTO GENERIC_ERROR = new StatusDTO("Invalid credentials");

    static {
        GENERIC_ERROR.setCode(500);
        GENERIC_ERROR.setMessage("Generic error");
    }

    private final StatusDTO status;

    public GenericException(StatusDTO status) {
        this.status = status;
    }

    public GenericException(int code, String message) {
        this.status = createStatus(code, message);
    }

    private StatusDTO createStatus(int code, String message) {
        StatusDTO status = new StatusDTO("Invalid credentials");
        status.setCode(code);
        status.setMessage(message);
        status.setTraceId(java.util.UUID.randomUUID().toString());
        return status;
    }
}
