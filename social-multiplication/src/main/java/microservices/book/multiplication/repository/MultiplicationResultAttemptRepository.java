package microservices.book.multiplication.repository;

import java.util.List;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import org.springframework.data.repository.CrudRepository;

/**
 * This is the interface allows us to save and retrieve {@link MultiplicationResultAttempt}
 */
public interface MultiplicationResultAttemptRepository extends
    CrudRepository<MultiplicationResultAttempt, Long> {

  /**
   * Find the lasted 5 attempts for a given {@link User}, identified by their alias.
   *
   * @param userAlias the given {@link User#getAlias()}
   * @return the lasted 5 attempts for a given {@link User}, identified by their alias.
   */
  List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(final String userAlias);
}
