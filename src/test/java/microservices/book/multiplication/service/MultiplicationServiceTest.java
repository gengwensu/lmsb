package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
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

    @Mock
    private MultiplicationRepository multiplicationRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        multiplicationService =
                new MultiplicationServiceImpl(randomGeneratorService, attemptRepository, userRepository, multiplicationRepository);
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

    @Test
    public void retrieveStatsTest(){
        Multiplication multiplication = new Multiplication(50, 60);
        User user = new User("John_Doe");
        MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(user, multiplication, 3010, false);
        MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(user, multiplication, 3051, false);
        List<MultiplicationResultAttempt> latestAttempts = Lists.newArrayList(attempt1, attempt2);
        given(userRepository.findByAlias("Johm_Doe")).willReturn(Optional.empty());
        given(attemptRepository.findTop5ByUserAliasOrderByIdDesc("John_Doe")).willReturn(latestAttempts);
        //when
        List<MultiplicationResultAttempt> latestAttemptResult=multiplicationService.getStatsForUser("John_Doe");
        //Then
        assertThat(latestAttemptResult).isEqualTo(latestAttempts);
    }

}