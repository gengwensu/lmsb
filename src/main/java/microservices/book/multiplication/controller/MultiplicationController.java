package microservices.book.multiplication.controller;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.service.MultiplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/multiplications")
final class MultiplicationController {
    private MultiplicationService multiplicationService;

    MultiplicationController(final MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }

    @GetMapping("/random")
    Multiplication getAMultiplicationObject(){
        return multiplicationService.createMultiplicationObjectWithTwoRandomNum();
    }
}
