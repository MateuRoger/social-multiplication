package microservices.book.multiplication.controller;

import java.util.List;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {

  private final MultiplicationService multiplicationService;

  @Autowired
  MultiplicationResultAttemptController(final MultiplicationService multiplicationService) {
    this.multiplicationService = multiplicationService;
  }

  @PostMapping
  public ResponseEntity<MultiplicationResultAttempt> postResult(
      final @RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
    return ResponseEntity.ok(new MultiplicationResultAttempt(
        multiplicationResultAttempt.getUser(),
        multiplicationResultAttempt.getMultiplication(),
        multiplicationResultAttempt.getResultAttempt(),
        multiplicationService.checkAttempt(multiplicationResultAttempt)));
  }

  @GetMapping
  public ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(
      final @RequestParam("alias") String alias) {
    return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
  }

  @GetMapping("/{resultId}")
  ResponseEntity<MultiplicationResultAttempt> getResultById(final @PathVariable("resultId") Long resultId) {
    return ResponseEntity.ok(
        multiplicationService.getResultById(resultId).orElse(null)
    );
  }
}
