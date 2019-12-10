package microservices.book.multiplication.service;

/**
 * This interface provides all the allowed operations that a consumer can do to generate randomly factors.
 */
public interface RandomGeneratorService {

  /**
   * Generates a randomly-generated factor.
   *
   * @return a randomly-generated factor. It's always a number between 11 and 99.
   */
  int generateRandomFactor();

}