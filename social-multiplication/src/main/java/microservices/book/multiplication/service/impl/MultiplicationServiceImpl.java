package microservices.book.multiplication.service.impl;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import microservices.book.multiplication.service.MultiplicationService;
import microservices.book.multiplication.service.RandomGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class MultiplicationServiceImpl implements MultiplicationService {

  private final RandomGeneratorService randomGeneratorService;
  private final MultiplicationResultAttemptRepository attemptRepository;
  private final UserRepository userRepository;
  private final MultiplicationRepository multiplicationRepository;
  private final EventDispatcher eventDispatcher;

  /**
   * Constructor
   *
   * @param randomGeneratorService   the injected {@link RandomGeneratorService}
   * @param attemptRepository        the injected {@link MultiplicationResultAttemptRepository}
   * @param userRepository           the injected {@link UserRepository}
   * @param multiplicationRepository the injected {@link MultiplicationRepository}
   * @param eventDispatcher          the injected {@link EventDispatcher}
   */
  @Autowired
  public MultiplicationServiceImpl(
      final RandomGeneratorService randomGeneratorService,
      final MultiplicationResultAttemptRepository attemptRepository,
      final UserRepository userRepository,
      final MultiplicationRepository multiplicationRepository,
      final EventDispatcher eventDispatcher) {
    this.randomGeneratorService = randomGeneratorService;
    this.attemptRepository = attemptRepository;
    this.userRepository = userRepository;
    this.multiplicationRepository = multiplicationRepository;
    this.eventDispatcher = eventDispatcher;
  }

  @Override
  public Multiplication createRandomMultiplication() {
    return new Multiplication(randomGeneratorService.generateRandomFactor(),
        randomGeneratorService.generateRandomFactor());
  }

  @Transactional
  @Override
  public MultiplicationResultAttempt checkAttempt(final MultiplicationResultAttempt attempt) {
    // Checks if the user already exists for the alias
    final Optional<User> storedUser = userRepository.findByAlias(attempt.getUser().getAlias());
    final Optional<Multiplication> storedMultiplication = multiplicationRepository
        .findByFactorAAndFactorB(
            attempt.getMultiplication().getFactorA(),
            attempt.getMultiplication().getFactorB());

    // Avoid 'hack' attempts
    Assert.isTrue(!attempt.isCorrect(), "You can't send an attempt marked as correct!!!");

    // Checks if it's correct
    final boolean isCorrect = attempt.getResultAttempt()
        == attempt.getMultiplication().getFactorA()
        * attempt.getMultiplication().getFactorB();

    // Creates a copy, now setting the 'correct' field accordingly
    final MultiplicationResultAttempt checkedAttempt = new MultiplicationResultAttempt(
        storedUser.orElse(attempt.getUser()),
        storedMultiplication.orElse(attempt.getMultiplication()), attempt.getResultAttempt(),
        isCorrect);

    // Stores the attempt
    final MultiplicationResultAttempt storedAttempt = this.attemptRepository.save(checkedAttempt);

    // Communicates the result via Event
    this.eventDispatcher.send(
        new MultiplicationSolvedEvent(checkedAttempt.getId(),
            checkedAttempt.getUser().getId(),
            checkedAttempt.isCorrect()));

    return storedAttempt;
  }

  @Override
  public List<MultiplicationResultAttempt> getStatsForUser(final String userAlias) {
    return attemptRepository.findTop5ByUserAliasOrderByIdDesc(userAlias);
  }

  @Override
  public MultiplicationResultAttempt getResultById(final long attemptId) {
    return attemptRepository.findById(attemptId)
        .orElseThrow(() -> new IllegalArgumentException("The requested attemptId [" + attemptId + "] does not exist."));
  }
}
