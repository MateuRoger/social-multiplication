package microservices.book.multiplication.repository;

import java.util.Optional;
import microservices.book.multiplication.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * This is the interface allows us to save and retrieve {@link User}
 */
public interface UserRepository extends CrudRepository<User, Long> {

  /**
   * Retrieves an {@link User} by its alias.
   *
   * @param alias the given alias to find it
   * @return an {@link Optional<User>}
   */
  Optional<User> findByAlias(final String alias);
}
