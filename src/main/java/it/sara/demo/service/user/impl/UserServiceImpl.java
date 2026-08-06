package it.sara.demo.service.user.impl;

import it.sara.demo.exception.GenericException;
import it.sara.demo.service.database.UserRepository;
import it.sara.demo.service.database.model.User;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.AddUserResult;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.service.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    // StringUtil è una utility: NON serve autowiring.
    // @Autowired
    // private StringUtil stringUtil;  // rimosso

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddUserResult addUser(CriteriaAddUser criteria) throws GenericException {

        AddUserResult returnValue;
        User user;

        try {

            returnValue = new AddUserResult();

            if (StringUtil.isNullOrEmpty(criteria.getFirstName())) {
                throw new GenericException(400, "First name is required");
            }
            if (StringUtil.isNullOrEmpty(criteria.getLastName())) {
                throw new GenericException(400, "Last name is required");
            }
            // Validazioni "di servizio" (seconda linea di difesa)
            if (StringUtil.isNullOrEmpty(criteria.getEmail())) {
                throw new GenericException(400, "Email is required");
            }
            if (StringUtil.isNullOrEmpty(criteria.getPhoneNumber())) {
                throw new GenericException(400, "Phone is required");
            }

            user = new User();
            user.setFirstName(criteria.getFirstName());
            user.setLastName(criteria.getLastName());
            user.setEmail(criteria.getEmail());
            user.setPhoneNumber(criteria.getPhoneNumber());

            if (!userRepository.save(user)) {
                throw new GenericException(500, "Error saving user");
            }

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            throw new GenericException(GenericException.GENERIC_ERROR);
        }
        return returnValue;
    }



    @Override
    public GetUsersResult getUsers(CriteriaGetUsers criteria) throws GenericException {
        try {
            if (criteria == null) {
                throw new GenericException(400, "Criteria is required");
            }

            // 1) paginazione
            int offset = Math.max(0, criteria.getOffset());
            int limit = criteria.getLimit() <= 0 ? 20 : Math.min(criteria.getLimit(), 100);

            // 2) filtro case-insensitive
            String rawQuery = criteria.getQuery();
            String normalizedQuery = StringUtil.isNullOrEmpty(rawQuery) ? null : rawQuery.trim().toLowerCase();
            String likePattern = (normalizedQuery == null ? null : "%" + normalizedQuery + "%");

            // 3) ordinamento da enum (whitelist colonne)
            CriteriaGetUsers.OrderType order = criteria.getOrder() == null
                    ? CriteriaGetUsers.OrderType.BY_LASTNAME
                    : criteria.getOrder();

            String sortColumn;
            boolean sortDesc;
            switch (order) {
                case BY_FIRSTNAME:
                    sortColumn = "first_name";
                    sortDesc = false;
                    break;
                case BY_FIRSTNAME_DESC:
                    sortColumn = "first_name";
                    sortDesc = true;
                    break;
                case BY_LASTNAME:
                    sortColumn = "last_name";
                    sortDesc = false;
                    break;
                case BY_LASTNAME_DESC:
                    sortColumn = "last_name";
                    sortDesc = true;
                    break;
                default:
                    sortColumn = "last_name";
                    sortDesc = false;
            }

            // 4) totale + pagina
            long total = userRepository.countUsersByQuery(likePattern);
            java.util.List<it.sara.demo.service.database.model.User> items =
                    userRepository.searchUsersByQuery(likePattern, sortColumn, sortDesc, offset, limit);

            // 5) mapping a DTO
            java.util.List<it.sara.demo.dto.UserDTO> dtos = new java.util.ArrayList<>(items.size());
            for (it.sara.demo.service.database.model.User u : items) {
                it.sara.demo.dto.UserDTO dto = new it.sara.demo.dto.UserDTO();
                dto.setGuid(u.getGuid());
                dto.setFirstName(u.getFirstName());
                dto.setLastName(u.getLastName());
                dto.setEmail(u.getEmail());
                dto.setPhoneNumber(u.getPhoneNumber());
                dtos.add(dto);
            }

            // 6) result
            GetUsersResult result = new GetUsersResult();
            result.setUsers(dtos);
            result.setTotal(total);
            result.setOffset(offset);
            result.setLimit(limit);

            return result;

        } catch (GenericException ge) {
            throw ge;
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(e.getMessage(), e);
            }
            throw new GenericException(GenericException.GENERIC_ERROR);
        }
    }

}
