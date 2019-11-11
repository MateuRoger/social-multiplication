package microservices.book.multiplication.service;

import java.util.List;
import java.util.Optional;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;

public interface MultiplicationService {

  /**
   * Generates a random {@link Multiplication} object.
   *
   * @return a multiplication of randomly generated numbers.
   */
  Multiplication createRandomMultiplication();

  /**
   * Checks the result of the given {@link MultiplicationResultAttempt}.
   *
   * @param resultAttempt the given {@link MultiplicationResultAttempt} to be checked
   * @return true if the attempt matches the result of the  multiplication, false otherwise.
   */
  boolean checkAttempt(final MultiplicationResultAttempt resultAttempt);

  /**
   * Gets the statistics for a given {@link User}.
   *
   * @param userAlias the user's alias
   * @return a list of {@link MultiplicationResultAttempt} objects, being the past attempts of the
   * user.
   */
  List<MultiplicationResultAttempt> getStatsForUser(final String userAlias);

  /**
   * Gets the {@link MultiplicationResultAttempt} by its id
   *
   * @param attemptId the {@link MultiplicationResultAttempt} id
   * @return the {@link MultiplicationResultAttempt} corresponding with the given id
   */
  Optional<MultiplicationResultAttempt> getResultById(final long attemptId);
}