package microservices.book.gamification.event;

import java.io.Serializable;
import lombok.Value;

/**
 * Event received when a multiplication has been solved in the system.
 * </p>
 * Provides some context information about the multiplication.
 */
@Value
class MultiplicationSolvedEvent implements Serializable {

  private final Long multiplicationResultAttemptId;
  private final Long userId;
  private final boolean correct;

}