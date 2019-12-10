package microservices.book.multiplication.service;

import microservices.book.multiplication.domain.User;

/**
 * It provides methods for the user domain. This interface provides all the allowed operations that a consumer can do
 * with a User.
 */
public interface UserService {

  /**
   * Retrieves the {@link User} by its id.
   *
   * @param userId the {@link User} Id to be searched.
   * @return the {@link User} with the given id.
   */
  User getUserById(Long userId);
}
