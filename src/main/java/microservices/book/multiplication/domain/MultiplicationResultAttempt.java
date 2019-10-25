package microservices.book.multiplication.domain;

import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Identifies the attempt from a {@link User} to solve a {@link Multiplication}.
 */
@Entity
public class MultiplicationResultAttempt {

  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "USER_ID")
  private final User user;
  @ManyToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "MULTIPLICATIOM_ID")
  private final Multiplication multiplication;
  private final int resultAttempt;
  private final boolean correct;
  @Id
  @GeneratedValue
  private Long id;

  /**
   * Empty constructor for JSON/JPA
   */
  public MultiplicationResultAttempt() {
    user = null;
    multiplication = null;
    resultAttempt = -1;
    correct = false;
  }

  /**
   * Full constructor.
   *
   * @param user the {@link User} who do the attempt.
   * @param multiplication the {@link Multiplication} to done
   * @param resultAttempt the given result.
   * @param correct indicates whether the {@code resultAttempt} is correct or not
   */
  public MultiplicationResultAttempt(User user,
      Multiplication multiplication, int resultAttempt, boolean correct) {
    this.user = user;
    this.multiplication = multiplication;
    this.resultAttempt = resultAttempt;
    this.correct = correct;
  }

  public Long getId() {
    return id;
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
    MultiplicationResultAttempt attempt = (MultiplicationResultAttempt) o;
    return resultAttempt == attempt.resultAttempt &&
        correct == attempt.correct &&
        Objects.equals(user, attempt.user) &&
        Objects.equals(multiplication, attempt.multiplication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, multiplication, resultAttempt, correct);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", MultiplicationResultAttempt.class.getSimpleName() + "[",
        "]")
        .add("id=" + id)
        .add("user=" + user)
        .add("multiplication=" + multiplication)
        .add("resultAttempt=" + resultAttempt)
        .add("correct=" + correct)
        .toString();
  }
}
