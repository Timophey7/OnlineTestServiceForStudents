package com.securityservice.service;


import com.securityservice.model.AuthenticationResponse;
import com.securityservice.model.User;

import java.util.Optional;

public interface UserService {

    public AuthenticationResponse saveUser(User user);

    User findUserByToken(String token);

    User findUserByEmail(String email);


}
