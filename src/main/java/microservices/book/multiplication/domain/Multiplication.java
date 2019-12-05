package microservices.book.multiplication.domain;

import lombok.Data;

@Data
public class Multiplication {
    private int factorA;
    private int factorB;
    private int product;

    public Multiplication(int fA, int fB) {
        factorA = fA;
        factorB = fB;
        product = factorA * factorB;
    }
}
