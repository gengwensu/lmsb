package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import org.springframework.stereotype.Service;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

    private RandomGeneratorService randomGeneratorService;

    public MultiplicationServiceImpl(RandomGeneratorService randomGeneratorService) {
        this.randomGeneratorService = randomGeneratorService;
    }

    @Override
    public Multiplication createMultiplicationObjectWithTwoRandomNum() {
        int fa = randomGeneratorService.generateARandomNum();
        int fb = randomGeneratorService.generateARandomNum();
        return new Multiplication(fa, fb);
    }
}
