package com.userservice.controller;

import com.userservice.model.test.*;
import com.userservice.model.user.User;
import com.userservice.repository.AnswerOptionsRepository;
import com.userservice.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/v1/test")
public class PassTestController {

    private List<Question> questionsForVisit = new ArrayList<>();

    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerOptionsRepository answerOptionsRepository;
    private final PassTestService passTestService;
    private final UserService userService;


    @GetMapping("/passTest/{uniqueCode}")
    public String passTest(@PathVariable("uniqueCode") String uniqueCode, Model model, HttpSession session) {
        String email = (String) session.getAttribute("email");
        PassedTest byStudentEmailAndTestUniqueCode = passTestService.findByStudentEmailAndTestUniqueCode(email, uniqueCode);
        if (byStudentEmailAndTestUniqueCode == null) {
            Test testByUniqueCode = testService.findTestByUniqueCode(uniqueCode);
            List<Question> questionsByTestId = questionService.findQuestionByTestId(testByUniqueCode.getId());
            questionsForVisit = questionsByTestId;
            PassedTest passedTest = new PassedTest();
            passedTest.setAllQuestions(questionsForVisit.size());
            passedTest.setStudentEmail(email);
            passedTest.setTestUniqueCode(uniqueCode);
            passTestService.savePassedTestInfo(passedTest);
            Question first = questionsForVisit.get(0);

            List<AnswerOptions> options = first.getOptions();
            model.addAttribute("question", first);
            model.addAttribute("options", options);
            return "test";
        }else {
            if (questionsForVisit.size() == 0) {
                PassedTest byStudentEmailAndTestUniqueCode1 = passTestService.findByStudentEmailAndTestUniqueCode(email, uniqueCode);
                model.addAttribute("result", byStudentEmailAndTestUniqueCode1);
                return "results";
            }
            Question question = questionsForVisit.get(0);

            List<AnswerOptions> options = question.getOptions();
            model.addAttribute("question", question);
            model.addAttribute("options", options);
            return "test";
        }

    }


    @PostMapping("/answer/{testUniqueCode}")
    public String handleAnswer(@PathVariable("testUniqueCode") String testUniqueCode,
                               @RequestParam("optionId") Integer selectedOptionId,
                               Model model,
                               HttpSession session,
                               HttpServletResponse response) {

        AnswerOptions answerOptions = answerOptionsRepository.findById(selectedOptionId)
                .orElse(null);
        String email = (String) session.getAttribute("email");

        if (answerOptions.getIsCorrect()){

            PassedTest passedTest = passTestService.findByStudentEmailAndTestUniqueCode(email, testUniqueCode);
            passedTest.setRightAnswers(passedTest.getRightAnswers() + 1);
            passTestService.savePassedTestInfo(passedTest);
            questionsForVisit.remove(0);
            if (questionsForVisit.size() == 0) {
                String studentEmail = passedTest.getStudentEmail();
                User userByEmail = userService.findUserByToken(session);
                String fio = userByEmail.getFirstName() + " " + userByEmail.getLastName();
                int percent = passTestService.calculatePercent(passedTest.getRightAnswers(), passedTest.getRightAnswers());
                String grade = passTestService.grade(percent);
                passedTest.setFio(fio);
                passedTest.setGrade(grade);
                passedTest.setPercent(percent);
                passTestService.savePassedTestInfo(passedTest);
                model.addAttribute("result",passedTest);
                return "results";
            }else {
                return "redirect:/v1/test/passTest/" + testUniqueCode;
            }
        }
        questionsForVisit.remove(0);
        if (questionsForVisit.size() == 0) {
            PassedTest passedTest = passTestService.findByStudentEmailAndTestUniqueCode(email, testUniqueCode);
            String studentEmail = passedTest.getStudentEmail();
            User userByEmail = userService.findUserByToken(session);
            String fio = userByEmail.getFirstName() + " " + userByEmail.getLastName();
            int percent = passTestService.calculatePercent(passedTest.getRightAnswers(), passedTest.getRightAnswers());
            String grade = passTestService.grade(percent);
            passedTest.setFio(fio);
            passedTest.setGrade(grade);
            passedTest.setPercent(percent);
            passTestService.savePassedTestInfo(passedTest);
            model.addAttribute("result",passedTest);
            return "results";
        }else {
            return "redirect:/v1/test/passTest/" + testUniqueCode;
        }

    }





}
