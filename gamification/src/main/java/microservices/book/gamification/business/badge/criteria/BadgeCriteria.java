package microservices.book.gamification.business.badge.criteria;

import java.util.Optional;
import microservices.book.gamification.domain.Badge;

/**
 * Provides methods to obtain {@link Badge} according its criteria.
 */
@FunctionalInterface
public interface BadgeCriteria {

  Optional<Badge> applyCriteria();
}
