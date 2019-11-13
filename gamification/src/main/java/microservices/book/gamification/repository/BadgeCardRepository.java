package microservices.book.gamification.repository;

import java.util.List;
import microservices.book.gamification.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

/**
 * Handles data operations with {@link BadgeCard}
 */
public interface BadgeCardRepository extends CrudRepository<BadgeCard, Long> {

  /**
   * Retrieves all BadgeCards for a given user.
   *
   * @param userId the id of the user to look for BadgeCards
   * @return the list of BadgeCards, sorted by most recent.
   */
  List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);
}
