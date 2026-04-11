package it.sara.demo.web.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenericResponseTest {

    @Test
    void success_withMessage_shouldReturn200Status() {
        GenericResponse response = GenericResponse.success("User added.");

        assertNotNull(response.getStatus());
        assertEquals(200, response.getStatus().getCode());
        assertEquals("User added.", response.getStatus().getMessage());
        assertNotNull(response.getStatus().getTraceId());
    }

    @Test
    void success_withNullMessage_shouldFallbackToDefault() {
        GenericResponse response = GenericResponse.success(null);

        assertNotNull(response.getStatus());
        assertEquals(200, response.getStatus().getCode());
        assertEquals("Success", response.getStatus().getMessage());
        assertNotNull(response.getStatus().getTraceId());
    }
}
