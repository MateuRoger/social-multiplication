package microservices.book.gamification.event;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Event received when a multiplication has been solved in the system.
 * </p>
 * Provides some context information about the multiplication.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class MultiplicationSolvedEvent implements Serializable {

  private final Long multiplicationResultAttemptId;
  private final Long userId;
  private final boolean correct;

  Long getMultiplicationResultAttemptId() {
    return multiplicationResultAttemptId;
  }

  public Long getUserId() {
    return userId;
  }

  boolean isCorrect() {
    return correct;
  }

}
