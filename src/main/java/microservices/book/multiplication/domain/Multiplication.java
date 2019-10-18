package microservices.book.multiplication.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * This represents a Multiplication (a * b)
 */
public class Multiplication {

  // Both factors
  private final int factorA;
  private final int factorB;

  Multiplication() {
    this(0, 0);
  }

  public Multiplication(int factorA, int factorB) {
    this.factorA = factorA;
    this.factorB = factorB;
  }

  public int getFactorA() {
    return factorA;
  }

  public int getFactorB() {
    return factorB;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("factorA", factorA)
        .append("factorB", factorB)
        .toString();
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

    return new EqualsBuilder()
        .append(factorA, that.factorA)
        .append(factorB, that.factorB)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
        .append(factorA)
        .append(factorB)
        .toHashCode();
  }
}