package microservices.book.multiplication.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.service.MultiplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class provides a REST API to POST the attempts from users.
 */
@Slf4j
@RestController
@RequestMapping("/results")
final class MultiplicationResultAttemptController {

  private final MultiplicationService multiplicationService;
  private final int serverPort;

  /**
   * Constructor.
   *
   * @param multiplicationService the injected {@link MultiplicationService}.
   * @param serverPort            the server port of the service.
   */
  @Autowired
  MultiplicationResultAttemptController(final MultiplicationService multiplicationService,
      @Value("${server.port}") final int serverPort) {
    this.multiplicationService = multiplicationService;
    this.serverPort = serverPort;
  }

  @PostMapping
  public ResponseEntity<MultiplicationResultAttempt> postResult(
      final @RequestBody MultiplicationResultAttempt multiplicationResultAttempt) {
    log.info("Checking the attempt {} from server @ {}", multiplicationResultAttempt, serverPort);
    return ResponseEntity.ok(multiplicationService.checkAttempt(multiplicationResultAttempt));
  }

  @GetMapping
  public ResponseEntity<List<MultiplicationResultAttempt>> getStatistics(
      final @RequestParam("alias") String alias) {
    log.info("Getting statistics of {} from server @ {}", alias, serverPort);
    return ResponseEntity.ok(multiplicationService.getStatsForUser(alias));
  }

  @GetMapping("/{resultId}")
  ResponseEntity<MultiplicationResultAttempt> getResultById(final @PathVariable("resultId") Long resultId) {
    log.info("Retrieving result {} from server @ {}", resultId, serverPort);
    return ResponseEntity.ok(multiplicationService.getResultById(resultId));
  }
}
