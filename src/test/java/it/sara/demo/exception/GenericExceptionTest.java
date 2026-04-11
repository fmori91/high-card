package it.sara.demo.exception;

import it.sara.demo.dto.StatusDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class GenericExceptionTest {

    @Test
    void constructor_withStatusDTO_shouldExposeSameStatus() {
        StatusDTO status = new StatusDTO();
        status.setCode(418);
        status.setMessage("teapot");

        GenericException ex = new GenericException(status);

        assertSame(status, ex.getStatus());
    }

    @Test
    void constructor_withCodeAndMessage_shouldBuildStatusWithTraceId() {
        GenericException ex = new GenericException(400, "bad input");

        assertNotNull(ex.getStatus());
        assertEquals(400, ex.getStatus().getCode());
        assertEquals("bad input", ex.getStatus().getMessage());
        assertNotNull(ex.getStatus().getTraceId());
    }

    @Test
    void genericErrorConstant_shouldHaveCode500AndMessage() {
        assertNotNull(GenericException.GENERIC_ERROR);
        assertEquals(500, GenericException.GENERIC_ERROR.getCode());
        assertEquals("Generic error", GenericException.GENERIC_ERROR.getMessage());
    }
}
