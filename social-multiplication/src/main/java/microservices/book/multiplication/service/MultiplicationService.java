package microservices.book.multiplication.service;

import java.util.List;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;

/**
 * It provides methods for the multiplication domain. This interface provides all the allowed operations that a consumer
 * can do with a Multiplication.
 */
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
   * @return the attempt that has been stored into database
   */
  MultiplicationResultAttempt checkAttempt(final MultiplicationResultAttempt resultAttempt);

  /**
   * Gets the statistics for a given {@link User}.
   *
   * @param userAlias the user's alias
   * @return a list of {@link MultiplicationResultAttempt} objects, being the past attempts of the user.
   */
  List<MultiplicationResultAttempt> getStatsForUser(final String userAlias);

  /**
   * Gets the {@link MultiplicationResultAttempt} by its id
   *
   * @param attemptId the {@link MultiplicationResultAttempt} id
   * @return the {@link MultiplicationResultAttempt} corresponding with the given id
   */
  MultiplicationResultAttempt getResultById(final long attemptId);
}