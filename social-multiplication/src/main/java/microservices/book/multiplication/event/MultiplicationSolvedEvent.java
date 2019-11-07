package microservices.book.multiplication.event;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import microservices.book.multiplication.domain.Multiplication;

/**
 * Event that models the fact that a {@link Multiplication} has been solved in the system. Provides some context
 * information about the multiplication.
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class MultiplicationSolvedEvent implements Serializable {

  private final Long multiplicationResultAttemptId;
  private final Long userId;
  private final boolean correct;

  public Long getMultiplicationResultAttemptId() {
    return multiplicationResultAttemptId;
  }

  public Long getUserId() {
    return userId;
  }

  public boolean isCorrect() {
    return correct;
  }

}
