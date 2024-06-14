package com.userservice.model.test;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "questions")
public class Question implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    private Test test;
    private String uniqueCode;
    private String text;
    @Enumerated(EnumType.STRING)
    private QuestionType type;
    @Column(length = 3000)
    private String answer;
    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<AnswerOptions> options = new ArrayList<>();

    public void addOptions(AnswerOptions options) {
        this.options.add(options);
    }

}
