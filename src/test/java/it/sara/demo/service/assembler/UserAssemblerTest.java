package it.sara.demo.service.assembler;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.service.database.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserAssemblerTest {

    private final UserAssembler assembler = new UserAssembler();

    private User buildUser() {
        User user = new User();
        user.setGuid("guid-123");
        user.setFirstName("Mario");
        user.setLastName("Rossi");
        user.setEmail("mario.rossi@example.com");
        user.setPhoneNumber("+393331234567");
        return user;
    }

    @Test
    void toDTO_shouldMapGuid() {
        UserDTO dto = assembler.toDTO(buildUser());
        assertNotNull(dto);
        assertEquals("guid-123", dto.getGuid());
    }

    @Test
    void toDTO_shouldMapFirstName() {
        UserDTO dto = assembler.toDTO(buildUser());
        assertEquals("Mario", dto.getFirstName());
    }

    @Test
    void toDTO_shouldMapLastName() {
        UserDTO dto = assembler.toDTO(buildUser());
        assertEquals("Rossi", dto.getLastName());
    }

    @Test
    void toDTO_shouldReturnFullEmail() {
        UserDTO dto = assembler.toDTO(buildUser());
        assertEquals("mario.rossi@example.com", dto.getEmail());
    }

    @Test
    void toDTO_withSubdomainEmail_shouldPreserveFullEmail() {
        User user = buildUser();
        user.setEmail("user@mail.example.com");

        UserDTO dto = assembler.toDTO(user);

        assertEquals("user@mail.example.com", dto.getEmail());
    }
}
