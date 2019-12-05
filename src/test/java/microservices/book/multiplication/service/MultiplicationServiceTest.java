package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class MultiplicationServiceTest {
    private MultiplicationService multiplicationService;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        multiplicationService = new MultiplicationServiceImpl(randomGeneratorService);
    }

    @Test
    @DisplayName("createMultiplicationObjectWithTwoRandomNum")
    void createMultiplicationObjectWithTwoRandomNumTest() {
        given(randomGeneratorService.generateARandomNum()).willReturn(50, 30);
        Multiplication multiplication = multiplicationService.createMultiplicationObjectWithTwoRandomNum();
        assertThat(multiplication.getFactorA()).isEqualTo(50);
        assertThat(multiplication.getFactorB()).isEqualTo(30);
    }

    @Test
    @DisplayName("checkAttempt_whenCorrect_thenTrue")
    public void checkAttempt_whenCorrect_thenTrueTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John Doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000);
        boolean attemptResult=multiplicationService.checkAttempt(attempt);
        assertThat(attemptResult).isTrue();
    }

    @Test
    @DisplayName("checkAttempt_whenWrong_thenFalse")
    public void checkAttempt_whenWrong_thenFalseTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John Doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3100);
        boolean attemptResult=multiplicationService.checkAttempt(attempt);
        assertThat(attemptResult).isFalse();
    }

}