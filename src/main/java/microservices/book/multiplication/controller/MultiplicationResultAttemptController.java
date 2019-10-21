package microservices.book.multiplication.controller;

import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {

  private final MultiplicationService multiplicationService;

  @Autowired
  MultiplicationResultAttemptController(final MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  /**
   * Inner class that represents the result of the {@link MultiplicationResultAttempt}.
   */
  static final class ResultResponse {

    private final boolean correct;

    /**
     * Empty Constructor.
     */
    public ResultResponse() {
      correct = false;
    }

    /**
     * Parametrized Constructor.
     *
     * @param correct indicates if the {@link MultiplicationResultAttempt} is correct or not.
     */
    public ResultResponse(boolean correct) {
      this.correct = correct;
    }

    public boolean isCorrect() {
      return correct;
    }
  }

  @PostMapping
  ResponseEntity<ResultResponse> postResult(
      @RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
    return ResponseEntity.ok(
        new ResultResponse(multiplicationService.checkAttempt(multiplicationResultAttempt)));
  }
}
