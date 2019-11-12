package microservices.book.gamification.business.badge.cases;

import java.util.Optional;
import microservices.book.gamification.domain.Badge;

/**
 * Provides methods to obtain {@link Badge} according its criteria.
 */
@FunctionalInterface
public interface BadgeObtainable {

  Optional<Badge> obtain();
}
