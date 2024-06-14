package com.securityservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "student_room")
public class StudentRoom implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roomId;
    private String ownerEmail;
    private String roomName;
    private String uniqueCode;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> userList = new ArrayList<User>();

    public void addStudentInRoom(User user){
        userList.add(user);
    }

}
