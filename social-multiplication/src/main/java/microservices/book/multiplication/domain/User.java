package microservices.book.multiplication.domain;

import java.util.Objects;
import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Stores information to identify the user.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
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
}