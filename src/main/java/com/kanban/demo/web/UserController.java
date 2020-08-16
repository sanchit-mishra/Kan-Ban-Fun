package com.kanban.demo.web;

import com.kanban.demo.domain.User;
import com.kanban.demo.payload.JWTLoginSuccessResponse;
import com.kanban.demo.payload.LoginRequest;
import com.kanban.demo.security.JwtTokenProvider;
import com.kanban.demo.services.MapValidationErrorService;
import com.kanban.demo.services.UserService;
import com.kanban.demo.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.kanban.demo.security.SecurityConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationError(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSuccessResponse(true,jwt));
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        userValidator.validate(user,result);

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationError(result);
        if(errorMap != null) return errorMap;

        User newUser = userService.saveUser(user);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

}
