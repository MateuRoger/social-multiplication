package microservices.book.gamification.client;

import microservices.book.gamification.client.dto.MultiplicationResultAttempt;

/**
 * This interface allows us to connect to the Multiplication microservice. Note that it's agnostic to the way of
 * communication.
 */
public interface MultiplicationResultAttemptClient {

  /**
   * Retrieves the {@link MultiplicationResultAttempt} from the multiplication microservice through a Rest API call.
   *
   * @param multiplicationId the multiplication id
   * @return the {@link MultiplicationResultAttempt} of gamification microservice.
   */
  MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long multiplicationId);
}
