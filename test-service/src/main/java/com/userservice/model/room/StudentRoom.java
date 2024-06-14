package com.userservice.model.room;

import com.userservice.model.user.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class StudentRoom implements Serializable {

    private int roomId;
    private String ownerEmail;
    private String roomName;
    private String uniqueCode;
    private List<User> students;

}
