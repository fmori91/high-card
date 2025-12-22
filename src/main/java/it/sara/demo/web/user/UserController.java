package it.sara.demo.web.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import it.sara.demo.exception.GenericException;
import it.sara.demo.service.user.UserService;
import it.sara.demo.service.user.criteria.CriteriaAddUser;
import it.sara.demo.service.user.criteria.CriteriaGetUsers;
import it.sara.demo.service.user.result.GetUsersResult;
import it.sara.demo.web.assembler.AddUserAssembler;
import it.sara.demo.web.response.GenericResponse;
import it.sara.demo.web.user.request.AddUserRequest;
import it.sara.demo.web.user.response.GetUsersResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddUserAssembler addUserAssembler;

    @RequestMapping(value = {"/v1/user"}, method = RequestMethod.PUT)
    public ResponseEntity<GenericResponse> addUser(@Valid @RequestBody AddUserRequest request) throws GenericException {
        CriteriaAddUser criteria = addUserAssembler.toCriteria(request);
        userService.addUser(criteria);
        return ResponseEntity.ok(GenericResponse.success("User added."));
    }

    @RequestMapping(value = {"/v1/user"}, method = RequestMethod.POST)
    public ResponseEntity<GetUsersResponse> getUsers(@RequestBody CriteriaGetUsers criteriaGetUsers) throws GenericException {
        GetUsersResult response = userService.getUsers(criteriaGetUsers);
        return ResponseEntity.ok(new GetUsersResponse(response));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<GenericResponse> login(@RequestBody Map<String, String> req) {
        if (!"admin".equals(req.get("username")) || !"admin".equals(req.get("password"))) {
            return ResponseEntity.ok(GenericResponse.error("Invalid credentials"));
        }

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(60 * 60);
        String token = Jwts.builder()
                .setSubject(req.get("username"))
                .setIssuer("sara.it")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .claim("policy", "USER_MGMT")
                .signWith(SignatureAlgorithm.HS256, "super-secret-key".getBytes())
                .compact();

        return ResponseEntity.ok(GenericResponse.success(Map.of("token", token).toString()));
    }

}
