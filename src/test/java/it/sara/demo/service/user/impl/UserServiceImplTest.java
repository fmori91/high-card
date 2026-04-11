package it.sara.demo.service.user.impl;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.service.util.StringUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Spy
    private StringUtil stringUtil = new StringUtil();

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl service;

    private CriteriaAddUser validCriteria() {
        CriteriaAddUser c = new CriteriaAddUser();
        c.setFirstName("Mario");
        c.setLastName("Rossi");
        c.setEmail("mario.rossi@example.com");
        c.setPhoneNumber("+393331234567");
        return c;
    }

    @Test
    void addUser_withValidData_shouldSaveAndReturnResult() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(true);

        AddUserResult result = service.addUser(validCriteria());

        assertNotNull(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addUser_withMissingFirstName_shouldThrow() {
        CriteriaAddUser c = validCriteria();
        c.setFirstName(null);

        assertThrows(GenericException.class, () -> service.addUser(c));
        verifyNoInteractions(userRepository);
    }

    @Test
    void addUser_withEmptyLastName_shouldThrow() {
        CriteriaAddUser c = validCriteria();
        c.setLastName("");

        assertThrows(GenericException.class, () -> service.addUser(c));
        verifyNoInteractions(userRepository);
    }

    @Test
    void addUser_withMissingEmail_shouldThrow() {
        CriteriaAddUser c = validCriteria();
        c.setEmail(null);

        assertThrows(GenericException.class, () -> service.addUser(c));
        verifyNoInteractions(userRepository);
    }

    @Test
    void addUser_withMissingPhone_shouldThrow() {
        CriteriaAddUser c = validCriteria();
        c.setPhoneNumber(null);

        assertThrows(GenericException.class, () -> service.addUser(c));
        verifyNoInteractions(userRepository);
    }

    @Test
    void addUser_whenRepositorySaveFails_shouldThrow() {
        when(userRepository.save(any(User.class))).thenReturn(false);

        assertThrows(GenericException.class, () -> service.addUser(validCriteria()));
    }

    @Test
    void getUsers_currentImplementation_shouldReturnNull() throws Exception {
        GetUsersResult result = service.getUsers(new CriteriaGetUsers());
        assertNull(result);
    }
}
