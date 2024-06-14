package com.userservice.controller;

import com.userservice.model.test.AnswerOptions;
import com.userservice.model.test.PassedTest;
import com.userservice.model.test.Question;
import com.userservice.model.user.User;
import com.userservice.repository.AnswerOptionsRepository;
import com.userservice.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = PassTestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PassTestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TestService testService;
    @MockBean
    private QuestionService questionService;
    @MockBean
    private AnswerOptionsRepository answerOptionsRepository;
    @MockBean
    private PassTestService passTestService;
    @MockBean
    private UserService userService;

    private static final String UNIQUE_CODE = "uniqueCode";
    private static final int SELECTED_OPTION_ID = 1;
    private static final String EMAIL = "test@example.com";

    PassedTest passedTest;

    @BeforeEach
    void setUp() {
        passedTest = new PassedTest();
        passedTest.setId(1);
        passedTest.setTestUniqueCode("uniqueCode");
    }

    @Test
    void passTestShouldCreateNewTest() throws Exception {
        com.userservice.model.test.Test test = new com.userservice.model.test.Test();
        test.setId(1);
        Question question = new Question();
        question.setId(1);
        question.setTest(test);
        AnswerOptions option = new AnswerOptions();
        option.setId(1);
        option.setQuestion(question);
        List<AnswerOptions> options = Arrays.asList(option);
        question.setOptions(options);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", EMAIL);

        when(testService.findTestByUniqueCode(UNIQUE_CODE)).thenReturn(test);
        when(questionService.findQuestionByTestId(test.getId())).thenReturn(Arrays.asList(question));
        doNothing().when(passTestService).savePassedTestInfo(any());

        ResultActions perform = mockMvc.perform(get("/v1/test/passTest/" + UNIQUE_CODE)
                .session(session)
                .accept(MediaType.TEXT_HTML));

        perform.andExpect(status().isOk())
                .andExpect(view().name("test"))
                .andExpect(model().attributeExists("question"))
                .andExpect(model().attributeExists("options"));
    }

    @Test
    void passTestShouldReturnNextQuestion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", EMAIL);

        com.userservice.model.test.Test test = new com.userservice.model.test.Test();
        test.setId(1);
        Question question = new Question();
        question.setId(1);
        question.setTest(test);
        AnswerOptions option = new AnswerOptions();
        option.setId(1);
        option.setQuestion(question);
        List<AnswerOptions> options = Arrays.asList(option);
        question.setOptions(options);

        com.userservice.model.test.PassedTest passedTest = new com.userservice.model.test.PassedTest();
        passedTest.setStudentEmail(EMAIL);
        passedTest.setTestUniqueCode(UNIQUE_CODE);

        when(passTestService.findByStudentEmailAndTestUniqueCode(EMAIL, UNIQUE_CODE)).thenReturn(passedTest);

        ResultActions perform = mockMvc.perform(get("/v1/test/passTest/" + UNIQUE_CODE)
                .session(session)
                .accept(MediaType.TEXT_HTML));

        perform.andExpect(status().isOk())
                .andExpect(model().attributeExists("question"));
    }

    @Test
    void passTestShouldReturnResults() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", EMAIL);

        com.userservice.model.test.PassedTest passedTest = new com.userservice.model.test.PassedTest();
        passedTest.setStudentEmail(EMAIL);
        passedTest.setTestUniqueCode(UNIQUE_CODE);

        when(passTestService.findByStudentEmailAndTestUniqueCode(EMAIL, UNIQUE_CODE)).thenReturn(passedTest);

        ResultActions perform = mockMvc.perform(get("/v1/test/passTest/" + UNIQUE_CODE)
                .session(session)
                .accept(MediaType.TEXT_HTML));

        perform.andExpect(status().isOk())
                .andExpect(view().name("results"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    void handleAnswerShouldReturnStatusIsOk() throws Exception {
        AnswerOptions answerOptions = new AnswerOptions();
        answerOptions.setIsCorrect(true);

        PassedTest passedTest = new PassedTest();
        passedTest.setStudentEmail(EMAIL);
        passedTest.setTestUniqueCode(UNIQUE_CODE);
        passedTest.setRightAnswers(0);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", EMAIL);

        when(answerOptionsRepository.findById(SELECTED_OPTION_ID)).thenReturn(java.util.Optional.of(answerOptions));
        when(passTestService.findByStudentEmailAndTestUniqueCode(EMAIL, UNIQUE_CODE)).thenReturn(passedTest);
        when(userService.findUserByToken(session)).thenReturn(user);
        when(passTestService.calculatePercent(anyInt(), anyInt())).thenReturn(80);
        when(passTestService.grade(80)).thenReturn("4");

        ResultActions perform = mockMvc.perform(post("/v1/test/answer/" + UNIQUE_CODE)
                .param("optionId", String.valueOf(SELECTED_OPTION_ID))
                .session(session)
                .accept(MediaType.TEXT_HTML));

        perform.andExpect(status().isOk())
                .andExpect(view().name("results"))
                .andExpect(model().attributeExists("result"));
    }

    @Test
    void handleAnswerShouldReturnStatusIsRedirect() throws Exception {
        AnswerOptions answerOptions = new AnswerOptions();
        answerOptions.setIsCorrect(false);

        PassedTest passedTest = new PassedTest();
        passedTest.setStudentEmail(EMAIL);
        passedTest.setTestUniqueCode(UNIQUE_CODE);
        passedTest.setRightAnswers(0);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("email", EMAIL);

        when(answerOptionsRepository.findById(SELECTED_OPTION_ID)).thenReturn(java.util.Optional.of(answerOptions));
        when(passTestService.findByStudentEmailAndTestUniqueCode(EMAIL, UNIQUE_CODE)).thenReturn(passedTest);
        when(userService.findUserByToken(session)).thenReturn(user);
        when(passTestService.calculatePercent(anyInt(), anyInt())).thenReturn(60);
        when(passTestService.grade(60)).thenReturn("3");

        ResultActions perform = mockMvc.perform(post("/v1/test/answer/" + UNIQUE_CODE)
                .param("optionId", String.valueOf(SELECTED_OPTION_ID))
                .session(session)
                .accept(MediaType.TEXT_HTML));

        perform.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/v1/test/passTest/" + UNIQUE_CODE));
    }
}