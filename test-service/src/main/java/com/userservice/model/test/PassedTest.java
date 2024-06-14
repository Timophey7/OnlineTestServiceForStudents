package com.userservice.model.test;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "passed_test_users")
public class PassedTest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String testUniqueCode;
    private int allQuestions;
    private int rightAnswers;
    private String studentEmail;
    private String fio;
    private String grade;
    private int percent;

}
