package microservices.book.multiplication.event;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;
import microservices.book.multiplication.domain.Multiplication;

/**
 * Event that models the fact that a {@link Multiplication} has been solved in the system. Provides
 * some context information about the multiplication.
 */
public class MultiplicationSolvedEvent implements Serializable {

  private final Long multiplicationResultAttemptId;
  private final Long userId;
  private final boolean correct;

  /**
   * Parametrized constructor
   */
  public MultiplicationSolvedEvent(Long multiplicationResultAttemptId, Long userId,
      boolean correct) {
    this.multiplicationResultAttemptId = multiplicationResultAttemptId;
    this.userId = userId;
    this.correct = correct;
  }

  public Long getMultiplicationResultAttemptId() {
    return multiplicationResultAttemptId;
  }

  public Long getUserId() {
    return userId;
  }

  public boolean isCorrect() {
    return correct;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultiplicationSolvedEvent that = (MultiplicationSolvedEvent) o;
    return correct == that.correct &&
        Objects.equals(multiplicationResultAttemptId, that.multiplicationResultAttemptId) &&
        Objects.equals(userId, that.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(multiplicationResultAttemptId, userId, correct);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MultiplicationSolvedEvent.class.getSimpleName() + "[",
        "]")
        .add("multiplicationResultAttemptId=" + multiplicationResultAttemptId)
        .add("userId=" + userId)
        .add("correct=" + correct)
        .toString();
  }
}
