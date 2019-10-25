package microservices.book.multiplication.domain;

import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * This represents a Multiplication (a * b).
 */
@Entity
public class Multiplication {

  // Both factors
  private final int factorA;
  private final int factorB;
  @Id
  @GeneratedValue
  @Column(name = "MULTIPLICATION_ID")
  private Long id;

  /**
   * Empty constructor for JSON/JPA
   */
  Multiplication() {
    this(0, 0);
  }

  /**
   * Parametrized Constructor
   *
   * @param factorA the factor A
   * @param factorB the factor B
   */
  public Multiplication(int factorA, int factorB) {
    this.factorA = factorA;
    this.factorB = factorB;
  }

  public Long getId() {
    return id;
  }

  public int getFactorA() {
    return factorA;
  }

  public int getFactorB() {
    return factorB;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Multiplication that = (Multiplication) o;
    return factorA == that.factorA &&
        factorB == that.factorB;
  }

  @Override
  public int hashCode() {
    return Objects.hash(factorA, factorB);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Multiplication.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("factorA=" + factorA)
        .add("factorB=" + factorB)
        .toString();
  }
}