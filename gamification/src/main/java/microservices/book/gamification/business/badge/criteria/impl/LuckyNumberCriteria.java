package microservices.book.gamification.business.badge.criteria.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import microservices.book.gamification.business.badge.criteria.BadgeCriteria;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;

/**
 * Represents the criteria to determine if the user can get the {@link Badge#LUCKY_NUMBER}
 */
@RequiredArgsConstructor
public class LuckyNumberCriteria implements BadgeCriteria {

  private static final int LUCKY_NUMBER = 42;

  private final List<BadgeCard> badgeCards;
  private final MultiplicationResultAttempt attempt;
  private final Badge badgeToBeChecked;

  @Override
  public Optional<Badge> applyCriteria() {
    Optional<Badge> optionalBadge = Optional.empty();

    if (badgeCards.stream().
        noneMatch(badge -> badge.getBadge().equals(badgeToBeChecked)) &&
        (attempt.getMultiplicationFactorA() == LUCKY_NUMBER || attempt.getMultiplicationFactorB() == LUCKY_NUMBER)) {
      optionalBadge = Optional.of(badgeToBeChecked);
    }

    return optionalBadge;
  }
}
