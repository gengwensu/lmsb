package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

import java.util.List;

public interface MultiplicationService {

    /**
     * create a Multiplication object with two randomly generated numbers btw 11 - 99
     * @return a Multiplication object
     */
    Multiplication createMultiplicationObjectWithTwoRandomNum();
    /**
     * @return true if the attempt matches the result of the
     * multiplication, false otherwise.
     */
    boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

    public List<MultiplicationResultAttempt> getStatsForUser(String userAlias);
}
