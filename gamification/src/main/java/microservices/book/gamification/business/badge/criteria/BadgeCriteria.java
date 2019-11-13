package microservices.book.gamification.business.badge.criteria;

import java.util.Optional;
import microservices.book.gamification.domain.Badge;

/**
 * Provides methods to obtain {@link Badge} according its criteria.
 */
@FunctionalInterface
public interface BadgeCriteria {

  /**
   * Applies the criteria on a {@link Badge} to know if it is possible to obtain or not.
   *
   * @return a {@link Optional<Badge>} if it is possible to obtain. Else returns a {@link Optional#empty()}
   */
  Optional<Badge> applyCriteria();
}
