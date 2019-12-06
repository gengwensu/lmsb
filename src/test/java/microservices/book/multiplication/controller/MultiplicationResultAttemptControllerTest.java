package microservices.book.multiplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@WebMvcTest(MultiplicationResultAttemptController.class)
public class MultiplicationResultAttemptControllerTest {
    @MockBean
    private MultiplicationService multiplicationService;

    @Autowired
    private MockMvc mvc;

    private JacksonTester<MultiplicationResultAttempt> jsonResult;

    @BeforeAll
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void postResult_ok_correct() throws Exception {
        genericParametrizedTest(true);
    }

    @Test
    public void postResult_ok_notCorrect() throws Exception {
        genericParametrizedTest(false);
    }

    void genericParametrizedTest(final boolean correct) throws Exception {
        given(multiplicationService
                .checkAttempt(any(MultiplicationResultAttempt.class)))
                .willReturn((correct));
        User user = new User("John Doe");
        Multiplication multiplication = new Multiplication(50, 70);
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
                user, multiplication, 3500, correct);
        //when
        MockHttpServletResponse response=mvc.perform(
                post("/results").contentType(MediaType.APPLICATION_JSON)
                .content(jsonResult.write(attempt).getJson()))
                .andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(
                jsonResult.write(new MultiplicationResultAttempt(
                        attempt.getUser(),
                        attempt.getMultiplication(),
                        attempt.getResultAttempt(),
                        correct)).getJson());
    }
}