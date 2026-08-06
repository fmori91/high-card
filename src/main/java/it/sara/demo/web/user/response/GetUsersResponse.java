package it.sara.demo.web.user.response;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.web.response.GenericPagedResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetUsersResponse extends GenericPagedResponse {
    private List<UserDTO> users;

    public GetUsersResponse(GetUsersResult result) {
        this.users = result.getUsers();
    }
}
