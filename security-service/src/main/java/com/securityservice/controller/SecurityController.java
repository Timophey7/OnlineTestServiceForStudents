package com.securityservice.controller;


import com.securityservice.model.*;
import com.securityservice.service.JwtService;
import com.securityservice.service.RoomService;
import com.securityservice.service.UserService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/test/security")
@RequiredArgsConstructor
@Slf4j
public class SecurityController {

    private final JwtService jwtService;
    private final RoomService roomService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


   @PostMapping("/registration")
   @Timed("registrationView")
   public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody User user) {
       AuthenticationResponse authenticationResponse = userService.saveUser(user);
       return new ResponseEntity<>(
               authenticationResponse,
               HttpStatus.CREATED
       );
   }


    @GetMapping("/getUserByToken")
    public User getUserByToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return userService.findUserByToken(token);
        } else {
            throw new UsernameNotFoundException("Некорректный заголовок Authorization");
        }
    }
   @PostMapping("/verify")
   @Timed("verifyView")
    public AuthenticationResponse verifyUser(@RequestBody AuthenticationRequest authenticationRequest) {
       User user = userService.findUserByEmail(authenticationRequest.getEmail());
       if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
           return new AuthenticationResponse();
       }
       String token = jwtService.generateToken(user);
       return new AuthenticationResponse(token);
   }


   @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(@RequestBody StudentRoom room) {
       roomService.save(room);
       return ResponseEntity.ok("success");
   }

   @GetMapping("/getRooms/{ownerEmail}")
    public ResponseEntity<List<StudentRoom>> getRooms(@PathVariable String ownerEmail) {
       List<StudentRoom> allByOwnerEmail = roomService.findAllByOwnerEmail(ownerEmail);
       return ResponseEntity.ok(allByOwnerEmail);
   }

   @GetMapping("/getRoomInfo/{uniqueCode}")
    public ResponseEntity<StudentRoom> getRoomInfo(@PathVariable String uniqueCode) {
       StudentRoom studentRoom = roomService.findByUniqueCode(uniqueCode).orElse(null);
       return ResponseEntity.ok(studentRoom);
   }

   @PostMapping("/saveStudentInRoom/{uniqueCode}")
    public String saveStudentInRoom(@PathVariable String uniqueCode, @RequestBody User user) {
       StudentRoom studentRoom = roomService.findByUniqueCode(uniqueCode).orElse(null);
       if (studentRoom != null) {
           studentRoom.addStudentInRoom(user);
       }
       roomService.save(studentRoom);
       return "success";
   }

   @GetMapping("/getStudentRooms/{studentId}")
    public ResponseEntity<List<StudentRoom>> getStudentRooms(@PathVariable int studentId) {
       List<StudentRoom> allStudentRooms = roomService.getAllStudentRooms(studentId);
       return ResponseEntity.ok(allStudentRooms);
   }


}
