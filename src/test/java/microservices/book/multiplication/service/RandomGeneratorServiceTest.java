package microservices.book.multiplication.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class RandomGeneratorServiceTest {
    private RandomGeneratorService randomGeneratorService;

    @BeforeEach
    void setUp() {
        randomGeneratorService = new RandomGeneratorServiceImpl();
    }

    @Test
    @DisplayName("generateARandomNum_whenIsBetweenExpectedLimits")
    void generateARandomNum_whenIsBetweenExpectedLimits() throws Exception {
        List<Integer> randomNumList = IntStream.range(0, 1000)
                .map(i->randomGeneratorService.generateARandomNum())
                .boxed().collect(Collectors.toList());

        assertThat(randomNumList).containsOnlyElementsOf(
                IntStream.range(11,100)
                .boxed().collect(Collectors.toList()));
    }
}