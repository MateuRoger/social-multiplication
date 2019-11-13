package microservices.book.gamification.business.badge;

import java.util.Optional;
import microservices.book.gamification.domain.Badge;

/**
 * Provides methods to execute operations on the {@link Badge}
 */
@FunctionalInterface
public interface BadgeOperation {

  /**
   * Execute the {@link Badge} operation. The operation object is responsible for knowing what to do.
   *
   * @return a {@link Badge} if this {@link Badge} has been executed successfully.
   */
  Optional<Badge> execute();
}
