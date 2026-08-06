package it.sara.demo.web.response;

import it.sara.demo.dto.StatusDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericResponse {
    private StatusDTO status;

    public static GenericResponse success(String message) {
        GenericResponse returnValue = new GenericResponse();
        returnValue.setStatus(new StatusDTO("Invalid credentials"));
        returnValue.getStatus().setCode(200);
        returnValue.getStatus().setMessage(message != null ? message : "Success");
        returnValue.getStatus().setTraceId(java.util.UUID.randomUUID().toString());
        return returnValue;
    }

    public static GenericResponse error(String message) {
        GenericResponse response = new GenericResponse();
        StatusDTO status = new StatusDTO();
        status.setCode(500);
        status.setMessage(message != null ? message : "Error");
        status.setTraceId(java.util.UUID.randomUUID().toString());
        response.setStatus(status);
        return response;
    }

}
