package com.userservice.service;

import com.userservice.model.user.AuthenticationRequest;
import com.userservice.model.user.AuthenticationResponse;
import com.userservice.model.user.User;
import com.userservice.model.user.VerifyResponse;
import jakarta.servlet.http.HttpSession;

public interface UserService {

    User findUserByToken(HttpSession session);

    AuthenticationResponse registration(User user, HttpSession session);

    String verify(AuthenticationRequest authenticationRequest,HttpSession session);
}
