package microservices.book.gamification.business.badge.impl;

import java.util.Optional;
import microservices.book.gamification.business.badge.BadgeOperation;
import microservices.book.gamification.business.badge.criteria.BadgeObtainable;
import microservices.book.gamification.domain.Badge;

/**
 * Represents the obtain operation on a {@link Badge}
 */
public class ObtainBadgeOperation implements BadgeOperation {

  private BadgeObtainable badgeObtainable;

  public ObtainBadgeOperation(BadgeObtainable badgeObtainable) {
    this.badgeObtainable = badgeObtainable;
  }

  @Override
  public Optional<Badge> execute() {
    return badgeObtainable.obtain();
  }
}
