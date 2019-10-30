package microservices.book.gamification.repository;

import java.util.List;
import microservices.book.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

/**
 * Handles data operations with {@link BadgeCard}
 */
public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

  List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
