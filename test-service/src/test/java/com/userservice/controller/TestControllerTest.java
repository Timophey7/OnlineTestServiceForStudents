package com.userservice.controller;

import com.userservice.model.test.AnswerOptions;
import com.userservice.model.test.PassedTest;
import com.userservice.model.test.Question;
import com.userservice.model.test.QuestionType;
import com.userservice.repository.PassedTestRepository;
import com.userservice.service.HashGenerator;
import com.userservice.service.QuestionService;
import com.userservice.service.TestService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.validation.BindingResult;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private PassedTestRepository passedTestRepository;
    @MockBean
    private HashGenerator hashGenerator;

    Question question;
    com.userservice.model.test.Test test;

    @BeforeEach
    public void setUp() {
        test = new com.userservice.model.test.Test();
        test.setId(1);
        test.setName("test");
        test.setUniqueCode("uniqueCode");
        test.setCreatorEmail("test@test.com");
        question = new Question();
        question.setId(1);
        question.setText("How are you?");
        question.setUniqueCode("uniqueCode");
        question.setType(QuestionType.SINGLE_CHOICE);
    }

    @Test
    void createTestShouldReturnStatusIsOk() throws Exception {

        ResultActions perform = mockMvc.perform(get("/v1/test/createTest"));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("newTest"));

    }

    @Test
    void saveTestShouldReturnStatusIsRedirect() throws Exception {
        doNothing().when(testService).save(test);

        ResultActions perform = mockMvc.perform(post("/v1/test/saveTest")
                .requestAttr("test", test));

        perform.andExpect(status().is3xxRedirection());


    }

    @Test
    void createQuestionShouldReturnStatusIsOk() throws Exception {

        String uniqueCode = "uniqueCode";
        when(testService.findTestByUniqueCode("uniqueCode")).thenReturn(test);
        doNothing().when(testService).save(test);

        ResultActions perform = mockMvc.perform(get("/v1/test/createSingleQuestion/" + uniqueCode)
                .param("uniqueCode", uniqueCode));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("question"));

    }

    @Test
    void saveQuestionShouldReturnStatusIsRedirect() throws Exception {
        doNothing().when(questionService).updateByUniqueCode(question.getUniqueCode(),question);

        ResultActions perform = mockMvc.perform(post("/v1/test/saveQuestion")
                .requestAttr("question", question));

        perform.andExpect(status().is3xxRedirection());
    }


    @Test
    void createOptionShouldReturnStatusIsOkAndContainsAllAttribute() throws Exception {

        String uniqueCode = "uniqueCode";

        when(questionService.findQuestionByUniqueCode(uniqueCode)).thenReturn(question);
        when(testService.findTestIdByQuestionId(question.getId())).thenReturn(test.getId());
        when(testService.findById(test.getId())).thenReturn(test);

        ResultActions perform = mockMvc.perform(get("/v1/test/createOption/uniqueCode")
                .param("uniqueCode", uniqueCode));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("option"))
                .andExpect(model().attribute("uniqueCode", uniqueCode));

    }

    @Test
    void saveOption() throws Exception {

        String uniqueCode = "uniqueCode";
        AnswerOptions answerOptions = new AnswerOptions();
        answerOptions.setId(1);
        answerOptions.setText("Im ok!");
        answerOptions.setIsCorrect(true);

        when(questionService.findQuestionByUniqueCode(uniqueCode)).thenReturn(question);
        doNothing().when(questionService).save(question);

        ResultActions perform = mockMvc.perform(post("/v1/test/createOption/uniqueCode")
                .param("uniqueCode", uniqueCode)
                .requestAttr("option", answerOptions));

        perform.andExpect(status().is3xxRedirection());
    }

    @Test
    void testUsers() throws Exception {
        String uniqueCode = "uniqueCode";
        PassedTest passedTest = new PassedTest();
        passedTest.setId(1);
        passedTest.setStudentEmail("test@gmail.com");
        when(passedTestRepository.findAllByTestUniqueCode(uniqueCode)).thenReturn(List.of(passedTest));

        ResultActions perform = mockMvc.perform(get("/v1/test/testUsers/" + uniqueCode)
                .param("uniqueCode", uniqueCode));

        perform.andExpect(status().isOk())
                .andExpect(model().attribute("students", List.of(passedTest)));


    }
}