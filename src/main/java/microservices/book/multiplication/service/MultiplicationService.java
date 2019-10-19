package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;

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
}