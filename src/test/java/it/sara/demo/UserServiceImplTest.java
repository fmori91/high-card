package it.sara.demo;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    @Test
    void addUser_success_callsRepositorySave() throws Exception {
        UserRepository repo = Mockito.mock(UserRepository.class);
        Mockito.when(repo.save(Mockito.any())).thenReturn(true);

        UserServiceImpl svc = new UserServiceImpl();
        // inject mock
        java.lang.reflect.Field f = UserServiceImpl.class.getDeclaredField("userRepository");
        f.setAccessible(true);
        f.set(svc, repo);

        CriteriaAddUser c = new CriteriaAddUser();
        c.setFirstName("Mario");
        c.setLastName("Rossi");
        c.setEmail("mario.rossi@example.com");
        c.setPhoneNumber("+393331234567");

        assertDoesNotThrow(() -> svc.addUser(c));
        Mockito.verify(repo, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void addUser_missingFirstName_throwsGenericException400() {
        UserRepository repo = Mockito.mock(UserRepository.class);
        UserServiceImpl svc = new UserServiceImpl();
        try {
            java.lang.reflect.Field f = UserServiceImpl.class.getDeclaredField("userRepository");
            f.setAccessible(true);
            f.set(svc, repo);
        } catch (Exception ignored) {
        }

        CriteriaAddUser c = new CriteriaAddUser();
        c.setLastName("Rossi");
        c.setEmail("mario.rossi@example.com");
        c.setPhoneNumber("+393331234567");

        GenericException ex = assertThrows(GenericException.class, () -> svc.addUser(c));
        assertEquals(400, ex.getStatus().getCode());
    }
}