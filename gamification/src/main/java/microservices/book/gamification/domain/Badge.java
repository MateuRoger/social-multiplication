package microservices.book.gamification.domain;

/**
 * Represents all the Badges that a user can obtain.
 */
public enum Badge {
  // Badges depending on score
  BRONZE_MULTIPLICATOR,
  SILVER_MULTIPLICATOR,
  GOLD_MULTIPLICATOR,

  // Other badges won for different conditions
  FIRST_ATTEMPT,
  FIRST_WON,
  LUCKY_NUMBER

}
