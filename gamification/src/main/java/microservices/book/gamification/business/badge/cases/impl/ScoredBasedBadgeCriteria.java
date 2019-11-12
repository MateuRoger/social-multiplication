package microservices.book.gamification.business.badge.cases.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import microservices.book.gamification.business.badge.cases.BadgeObtainable;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;

/**
 * Represents the criteria to determine if the user can get the {@link Badge} based on the user's score
 */
@RequiredArgsConstructor
public class ScoredBasedBadgeCriteria implements BadgeObtainable {

  private final List<BadgeCard> badgeCards;
  private final int minScore;
  private final int currentScore;
  private final Badge badgeToBeChecked;

  @Override
  public Optional<Badge> obtain() {
    Optional<Badge> optionalBadge = Optional.empty();

    if (badgeCards.stream().noneMatch(badge -> badge.getBadge().equals(badgeToBeChecked)) &&
        currentScore >= minScore) {
      optionalBadge = Optional.of(badgeToBeChecked);
    }

    return optionalBadge;
  }
}
