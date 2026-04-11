package it.sara.demo.web.user;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.web.assembler.AddUserAssembler;
import it.sara.demo.web.response.GenericResponse;
import it.sara.demo.web.user.request.AddUserRequest;
import it.sara.demo.web.user.request.GetUsersRequest;
import it.sara.demo.web.user.response.GetUsersResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AddUserAssembler addUserAssembler;

    @InjectMocks
    private UserController controller;

    private AddUserRequest buildRequest() {
        AddUserRequest request = new AddUserRequest();
        request.setFirstName("Mario");
        request.setLastName("Rossi");
        request.setEmail("mario.rossi@example.com");
        request.setPhoneNumber("+393331234567");
        return request;
    }

    @Test
    void addUser_withValidRequest_shouldDelegateAndReturn200() throws Exception {
        AddUserRequest request = buildRequest();
        CriteriaAddUser criteria = new CriteriaAddUser();
        when(addUserAssembler.toCriteria(request)).thenReturn(criteria);

        ResponseEntity<GenericResponse> response = controller.addUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getStatus());
        assertEquals(200, response.getBody().getStatus().getCode());
        assertEquals("User added.", response.getBody().getStatus().getMessage());
        verify(addUserAssembler).toCriteria(request);
        verify(userService).addUser(criteria);
    }

    @Test
    void addUser_whenServiceThrows_shouldPropagate() throws Exception {
        AddUserRequest request = buildRequest();
        when(addUserAssembler.toCriteria(any())).thenReturn(new CriteriaAddUser());
        doThrow(new GenericException(400, "boom")).when(userService).addUser(any());

        assertThrows(GenericException.class, () -> controller.addUser(request));
    }

    @Test
    void getUsers_currentImplementation_shouldReturn200WithEmptyBody() throws Exception {
        ResponseEntity<GetUsersResponse> response = controller.getUsers(new GetUsersRequest());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}
