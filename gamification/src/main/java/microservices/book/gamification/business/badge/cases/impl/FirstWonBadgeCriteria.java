package microservices.book.gamification.business.badge.cases.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import microservices.book.gamification.business.badge.cases.BadgeObtainable;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.ScoreCard;

/**
 * Represents the criteria to determine if the user can get the {@link Badge#FIRST_WON}
 */
@RequiredArgsConstructor
public class FirstWonBadgeCriteria implements BadgeObtainable {

  private final List<ScoreCard> scoreCards;
  private final List<BadgeCard> badgeCards;
  private final Badge badgeToBeChecked;

  @Override
  public Optional<Badge> obtain() {
    Optional<Badge> optionalBadge = Optional.empty();

    if (scoreCards.size() == 1 && badgeCards.stream()
        .noneMatch(badge -> badge.getBadge().equals(badgeToBeChecked))) {
      optionalBadge = Optional.of(badgeToBeChecked);
    }

    return optionalBadge;
  }
}
