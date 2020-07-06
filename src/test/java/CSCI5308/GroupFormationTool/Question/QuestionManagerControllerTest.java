package CSCI5308.GroupFormationTool.Question;

import CSCI5308.GroupFormationTool.Common.DomainConstants;
import CSCI5308.GroupFormationTool.Common.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(QuestionManagerController.class)
public class QuestionManagerControllerTest {

    public QuestionManagerRepository questionManagerRepository;
    public QuestionAdminRepository questionAdminRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        questionManagerRepository = mock(QuestionManagerRepository.class);
        Injector.instance().setQuestionManagerRepository(questionManagerRepository);
        questionAdminRepository = mock(QuestionAdminRepository.class);
        Injector.instance().setQuestionAdminRepository(questionAdminRepository);
    }

    @Test
    void createQuestion() throws Exception {
        this.mockMvc.perform(get("/questionManager/createQuestion"))
                .andExpect(status().isOk())
                .andExpect(view().name("question/createQuestion"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void saveQuestion() throws Exception {
        long outcome = 5;
        when(questionManagerRepository.createQuestion(any(Question.class))).thenReturn(outcome);

        this.mockMvc.perform(post("/questionManager/createQuestion")
                .param("questionTitle", "Sample title")
                .param("questionText", "Sample text")
                .param("questionType", String.valueOf(DomainConstants.numeric))
                .param("optionText", "")
                .param("optionValue", "")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/questionManager/viewQuestion?questionId=" + outcome))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        this.mockMvc.perform(post("/questionManager/createQuestion")
                .param("questionTitle", "")
                .param("questionText", "Sample text")
                .param("questionType", String.valueOf(DomainConstants.numeric))
                .param("optionText", "")
                .param("optionValue", "")
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("question/createQuestion"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    void deleteQuestion() throws Exception {
        long questionId = 1;

        when(questionManagerRepository.deleteQuestion(questionId)).thenReturn(true);
        when(questionAdminRepository.getQuestionListForInstructor("sample@gmail.com")).
                thenReturn(new ArrayList<>());
        this.mockMvc.perform(get("/questionManager/deleteQuestion")
                .param("questionId", String.valueOf(questionId)))
                .andExpect(status().isOk())
                .andExpect(view().name("question/questionManager"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        when(questionManagerRepository.deleteQuestion(questionId)).thenReturn(false);
        this.mockMvc.perform(get("/questionManager/deleteQuestion")
                .param("questionId", String.valueOf(questionId)))
                .andExpect(status().isOk())
                .andExpect(view().name("question/questionManager"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
