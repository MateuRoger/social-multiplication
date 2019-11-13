package microservices.book.gamification.business.badge.impl;

import java.util.Optional;
import microservices.book.gamification.business.badge.BadgeOperation;
import microservices.book.gamification.business.badge.criteria.BadgeCriteria;
import microservices.book.gamification.domain.Badge;

/**
 * Represents the obtain operation on a {@link Badge}
 */
public class ObtainBadgeOperation implements BadgeOperation {

  private final BadgeCriteria badgeCriteria;

  /**
   * The operation to obtain an specific Badge
   *
   * @param badgeCriteria the {@link BadgeCriteria} to be applied.
   */
  public ObtainBadgeOperation(final BadgeCriteria badgeCriteria) {
    this.badgeCriteria = badgeCriteria;
  }

  @Override
  public Optional<Badge> execute() {
    return badgeCriteria.applyCriteria();
  }
}
