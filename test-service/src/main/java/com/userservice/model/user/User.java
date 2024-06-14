package com.userservice.model.user;

import com.userservice.model.room.StudentRoom;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class User implements Serializable {

    private long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Role role;
    private Boolean isTeacher = false;
    private List<StudentRoom> studentRooms = new ArrayList<>();

}
