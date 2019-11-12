package microservices.book.gamification.business.badge;

import java.util.Optional;
import microservices.book.gamification.domain.Badge;

/**
 * Provides methods to execute operations on the {@link Badge}
 */
@FunctionalInterface
public interface BadgeOperation {

  Optional<Badge> execute();
}
