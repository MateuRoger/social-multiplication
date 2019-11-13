package microservices.book.multiplication.event;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Event that models the fact that a {@link microservices.book.multiplication.domain.Multiplication}
 * </p>
 * has been solved in the system. Provides some context information about the multiplication.
 */
@Getter
@ToString
@EqualsAndHashCode
public class MultiplicationSolvedEvent implements Serializable {

  private final Long multiplicationResultAttemptId;
  private final Long userId;
  private final boolean correct;

  public MultiplicationSolvedEvent(Long multiplicationResultAttemptId, Long userId, boolean correct) {
    this.multiplicationResultAttemptId = multiplicationResultAttemptId;
    this.userId = userId;
    this.correct = correct;
  }
}