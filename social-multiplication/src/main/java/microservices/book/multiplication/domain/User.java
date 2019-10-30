package microservices.book.multiplication.domain;

import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Stores information to identify the user.
 */
@Entity
public class User {

  private final String alias;
  @Id
  @GeneratedValue
  @Column(name = "USER_ID")
  private Long id;

  /**
   * Empty constructor for JSON/JPA
   */
  protected User() {
    alias = null;
  }

  /**
   * Parametrized Constructor
   *
   * @param alias the user's alias
   */
  public User(String alias) {
    this.alias = alias;
  }

  public Long getId() {
    return id;
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

    return alias != null && alias.equals(user.alias);
  }

  @Override
  public int hashCode() {
    return Objects.hash(alias);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
        .add("id=" + id)
        .add("alias='" + alias + "'")
        .toString();
  }
}