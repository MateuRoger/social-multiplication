package microservices.book.gamification.business.badge.criteria.impl;

import java.util.Optional;
import microservices.book.gamification.business.badge.criteria.BadgeCriteria;
import microservices.book.gamification.domain.Badge;

public class LuckStrikeCriteria implements BadgeCriteria {

  @Override
  public Optional<Badge> applyCriteria() {
    return Optional.empty();
  }
}
