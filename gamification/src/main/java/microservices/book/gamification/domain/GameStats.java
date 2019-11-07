package microservices.book.gamification.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * This object contains the result of one or many iterations of the game.
 * <p>
 * It may contain any combination of {@link ScoreCard} objects and {@link BadgeCard} objects.
 * </p> <p>
 * It can be used as a delta (as a single game iterations) or to represent the total amount of score / badges.
 * </p>
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class GameStats {

  private final Long userId;
  private final int score;
  private final List<Badge> badges;

  // Empty constructor for JSON / JPA
  public GameStats() {
    this.userId = 0L;
    this.score = 0;
    this.badges = new ArrayList<>();
  }

  /**
   * Factory to build an empty instance (zero points and no badges).
   *
   * @param userId the user's id
   * @return a {@link GameStats} object with zero score and no badges
   */
  public static GameStats emptyStats(final Long userId) {
    return new GameStats(userId, 0, Collections.emptyList());
  }

  /**
   * Gets an unmodifiable view of the {@link BadgeCard} list.
   *
   * @return an unmodifiable view of the {@link BadgeCard} list
   */
  public List<Badge> getBadges() {
    return Collections.unmodifiableList(badges);
  }
}
