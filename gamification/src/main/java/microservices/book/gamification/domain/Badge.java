package microservices.book.gamification.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Badge {
  // Badges depending on score
  BRONZE_MULTIPLICATOR(100),
  SILVER_MULTIPLICATOR(500),
  GOLD_MULTIPLICATOR(999),

  // Other badges won for different conditions
  FIRST_ATTEMPT(null),
  FIRST_WON(null),
  LUCKY_NUMBER(null);

  private final Integer minScoreToGet;
}
