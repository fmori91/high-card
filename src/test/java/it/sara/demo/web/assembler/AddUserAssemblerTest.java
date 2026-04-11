package it.sara.demo.web.assembler;

import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.web.user.request.AddUserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddUserAssemblerTest {

    private final AddUserAssembler assembler = new AddUserAssembler();

    private AddUserRequest buildRequest() {
        AddUserRequest request = new AddUserRequest();
        request.setFirstName("Mario");
        request.setLastName("Rossi");
        request.setEmail("mario.rossi@example.com");
        request.setPhoneNumber("+393331234567");
        return request;
    }

    @Test
    void toCriteria_withFullRequest_shouldMapAllFields() {
        CriteriaAddUser criteria = assembler.toCriteria(buildRequest());

        assertNotNull(criteria);
        assertEquals("Mario", criteria.getFirstName());
        assertEquals("Rossi", criteria.getLastName());
        assertEquals("mario.rossi@example.com", criteria.getEmail());
        assertEquals("+393331234567", criteria.getPhoneNumber());
    }

    @Test
    void toCriteria_lastName_shouldNotBeSourcedFromFirstName() {
        AddUserRequest request = buildRequest();
        request.setFirstName("A");
        request.setLastName("B");

        CriteriaAddUser criteria = assembler.toCriteria(request);

        assertNotEquals(criteria.getFirstName(), criteria.getLastName());
        assertEquals("B", criteria.getLastName());
    }

    @Test
    void toCriteria_withNullFields_shouldPropagateNulls() {
        AddUserRequest request = new AddUserRequest();

        CriteriaAddUser criteria = assembler.toCriteria(request);

        assertNotNull(criteria);
        assertNull(criteria.getFirstName());
        assertNull(criteria.getLastName());
        assertNull(criteria.getEmail());
        assertNull(criteria.getPhoneNumber());
    }
}
