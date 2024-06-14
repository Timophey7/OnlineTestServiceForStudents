package com.userservice.model.test;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tests")
public class Test implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String uniqueCode;
    private String creatorEmail;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Question> questions = new ArrayList<Question>();

    public void addQuestion(Question question) {
        questions.add(question);
    }

}
