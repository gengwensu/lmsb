package microservices.book.multiplication.controller;

import microservices.book.multiplication.service.MultiplicationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MultiplicationController {
    private MultiplicationService multiplicationService;

    MultiplicationController(final MultiplicationService multiplicationService) {
        this.multiplicationService = multiplicationService;
    }
}
