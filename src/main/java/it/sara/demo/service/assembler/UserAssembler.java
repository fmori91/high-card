package it.sara.demo.service.assembler;

import it.sara.demo.dto.UserDTO;
import it.sara.demo.service.database.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    public UserDTO toDTO(User user) {
        UserDTO returnValue = new UserDTO();
        returnValue.setEmail(user.getEmail());
        returnValue.setGuid(user.getGuid());
        returnValue.setFirstName(user.getFirstName());
        returnValue.setLastName(user.getLastName());
        return returnValue;
    }
}
