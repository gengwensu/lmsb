package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class MultiplicationServiceTest {
    private MultiplicationService multiplicationService;

    @Mock
    private RandomGeneratorService randomGeneratorService;

    @Mock
    private MultiplicationResultAttemptRepository attemptRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        multiplicationService =
                new MultiplicationServiceImpl(randomGeneratorService, attemptRepository,
                        userRepository);
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
        User user = new User("John_Doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3000, false);
        MultiplicationResultAttempt verifiedAttempt=new MultiplicationResultAttempt(user,multiplication,3000,true);
        given(userRepository.findByAlias("John-Doe")).willReturn(Optional.empty());
        boolean attemptResult = multiplicationService.checkAttempt(attempt);
        assertThat(attemptResult).isTrue();
        verify(attemptRepository).save(verifiedAttempt);
    }

    @Test
    @DisplayName("checkAttempt_whenWrong_thenFalse")
    public void checkAttempt_whenWrong_thenFalseTest() {
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John_Doe");
        MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication, 3100, false);
        given(userRepository.findByAlias("John_Doe")).willReturn(Optional.empty());
        boolean attemptResult = multiplicationService.checkAttempt(attempt);
        assertThat(attemptResult).isFalse();
        verify(attemptRepository).save(attempt);
    }

}