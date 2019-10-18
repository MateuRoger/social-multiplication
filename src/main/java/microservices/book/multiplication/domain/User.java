package microservices.book.multiplication.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Stores information to identify the user.
 */
public class User {

  private final String alias;

  // Empty constructor for JSON (de)serialization
  protected User() {
    alias = null;
  }

  public User(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    User user = (User) o;

    return new EqualsBuilder()
        .append(alias, user.alias)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(alias)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("alias", alias)
        .toString();
  }
}
