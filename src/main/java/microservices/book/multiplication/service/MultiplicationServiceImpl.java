package microservices.book.multiplication.service;

import lombok.RequiredArgsConstructor;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MultiplicationServiceImpl implements MultiplicationService {

    private final RandomGeneratorService randomGeneratorService;
    private final MultiplicationResultAttemptRepository attemptRepository;
    private final UserRepository userRepository;
    private final MultiplicationRepository multiplicationRepository;
    private final EventDispatcher eventDispatcher;

    @Override
    public Multiplication createMultiplicationObjectWithTwoRandomNum() {
        int fa = randomGeneratorService.generateARandomNum();
        int fb = randomGeneratorService.generateARandomNum();
        return new Multiplication(fa, fb);
    }

    @Transactional
    @Override
    public boolean checkAttempt(final MultiplicationResultAttempt attempt) {
        Optional<User> user = userRepository.findByAlias(attempt.getUser().getAlias());
        Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!!");
        boolean isCorrect = attempt.getResultAttempt() ==
                attempt.getMultiplication().getFactorA()
                        * attempt.getMultiplication().getFactorB();
        Optional<Multiplication> multiplication = multiplicationRepository.findByFactorAAndFactorB(
                attempt.getMultiplication().getFactorA(),
                attempt.getMultiplication().getFactorB()
        );
        MultiplicationResultAttempt checkedAttempt =
                new MultiplicationResultAttempt(user.orElse(attempt.getUser()),
                        multiplication.orElse(attempt.getMultiplication()),
                        attempt.getResultAttempt(),
                        isCorrect);
        attemptRepository.save(checkedAttempt);
        eventDispatcher.send(new MultiplicationSolvedEvent(checkedAttempt.getId(),
                checkedAttempt.getUser().getId(),
                checkedAttempt.isCorrect())
        );
        return isCorrect;
    }

    @Override
    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias) {
        return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
    }
}
