package com.userservice.controller;

import com.userservice.model.test.*;
import com.userservice.repository.PassedTestRepository;
import com.userservice.service.HashGenerator;
import com.userservice.service.QuestionService;
import com.userservice.service.TestService;
import io.micrometer.core.annotation.Timed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final QuestionService questionService;
    private final HashGenerator hashGenerator;
    private final TestService testService;
    private final PassedTestRepository passedTestRepository;

    @GetMapping("/createTest")
    @Timed("createTestMethod")
    public String createTest(Model model) {
        model.addAttribute("newTest", new Test());
        return "createTest";
    }

    @PostMapping("/saveTest")
    public String saveTest(@Valid  @ModelAttribute("test") Test test, BindingResult result,
                           Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "redirect:/v1/test/createTest";
        }
        String email =(String) session.getAttribute("email");
        test.setUniqueCode(hashGenerator.generateHash());
        test.setCreatorEmail(email);
        testService.save(test);
        return "redirect:/v1/test/createSingleQuestion/" + test.getUniqueCode();
    }

    @GetMapping("/createSingleQuestion/{uniqueCode}")
    @Timed("createQuestionMethod")
    public String createQuestion(@PathVariable("uniqueCode") String uniqueCode, Model model) {
        Test testByUniqueCode = testService.findTestByUniqueCode(uniqueCode);
        Question question = new Question();
        question.setTest(testByUniqueCode);
        question.setUniqueCode(hashGenerator.generateHash());
        question.setType(QuestionType.SINGLE_CHOICE);
        testByUniqueCode.addQuestion(question);
        testService.save(testByUniqueCode);
        model.addAttribute("question", question);
        return "createSingleQuestion";

    }

    @PostMapping("/saveQuestion")
    public String saveQuestion(@Valid @ModelAttribute("question") Question question, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "redirect:/v1/test/createSingleQuestion";
        }
        if (question.getText() != null){
            questionService.updateByUniqueCode(question.getUniqueCode(),question);
        }
        return "redirect:/v1/test/createOption/"+question.getUniqueCode();
    }

    @GetMapping("/createOption/{uniqueCode}")
    @Timed("createOptionMethod")
    public String createOption(@PathVariable("uniqueCode") String uniqueCode ,@ModelAttribute("message") String message, Model model) {
        if (message == null || message.isEmpty()) {
            model.addAttribute("message", "");
        }
        Question questionByUniqueCode = questionService.findQuestionByUniqueCode(uniqueCode);
        int testIdByQuestionId = testService.findTestIdByQuestionId(questionByUniqueCode.getId());
        Test test = testService.findById(testIdByQuestionId);
        model.addAttribute("uniqueCode", uniqueCode);
        model.addAttribute("message", message);
        model.addAttribute("option", new AnswerOptions());
        model.addAttribute("testUniqueCode", test.getUniqueCode());
        return "createOption";
    }

    @PostMapping("/createOption/{uniqueCode}")
    public String saveOption(@PathVariable("uniqueCode")String uniqueCode, @Valid @ModelAttribute("option") AnswerOptions option, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("message", "Возникла ошибка, попробуйте снова!");
            return "redirect:/v1/test/createOption";
        }
        model.addAttribute("message","Вариант ответа успешно сохранен!");
        Question questionByUniqueCode = questionService.findQuestionByUniqueCode(uniqueCode);
        option.setQuestion(questionByUniqueCode);
        questionByUniqueCode.addOptions(option);
        questionService.save(questionByUniqueCode);
        return "redirect:/v1/test/createOption/"+questionByUniqueCode.getUniqueCode();
    }

    @GetMapping("/testUsers/{uniqueCode}")
    public String testUsers(@PathVariable("uniqueCode") String uniqueCode, Model model,HttpServletRequest request) {

        List<PassedTest> allByTestUniqueCode = passedTestRepository.findAllByTestUniqueCode(uniqueCode);
        model.addAttribute("students", allByTestUniqueCode);
        return "studentsGrade";
    }

}
