package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;

public interface MultiplicationService {

    /**
     * create a Multiplication object with two randomly generated numbers btw 11 - 99
     * @return a Multiplication object
     */
    Multiplication createMultiplicationObjectWithTwoRandomNum();
}
