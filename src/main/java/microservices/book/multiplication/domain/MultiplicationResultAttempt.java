package microservices.book.multiplication.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Identifies the attempt from a {@link User} to solve a {@link Multiplication}.
 */
public class MultiplicationResultAttempt {

  private final User user;
  private final Multiplication multiplication;
  private final int resultAttempt;

  // Empty constructor for JSON (de)serialization

  /**
   * Empty Constructor.
   */
  public MultiplicationResultAttempt() {
    user = null;
    multiplication = null;
    resultAttempt = -1;
  }

  /**
   * Full constructor.
   * @param user the {@link User} who do the attempt.
   * @param multiplication the {@link Multiplication} to done
   * @param resultAttempt the given result.
   */
  public MultiplicationResultAttempt(User user,
      Multiplication multiplication, int resultAttempt) {
    this.user = user;
    this.multiplication = multiplication;
    this.resultAttempt = resultAttempt;
  }

  public User getUser() {
    return user;
  }

  public Multiplication getMultiplication() {
    return multiplication;
  }

  public int getResultAttempt() {
    return resultAttempt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MultiplicationResultAttempt attempt = (MultiplicationResultAttempt) o;

    return new EqualsBuilder()
        .append(resultAttempt, attempt.resultAttempt)
        .append(user, attempt.user)
        .append(multiplication, attempt.multiplication)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(user)
        .append(multiplication)
        .append(resultAttempt)
        .toHashCode();
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("user", user)
        .append("multiplication", multiplication)
        .append("resultAttempt", resultAttempt)
        .toString();
  }
}
