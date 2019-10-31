package microservices.book.gamification.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;

public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;

  public GameServiceImpl(ScoreCardRepository scoreCardRepository,
      BadgeCardRepository badgeCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
  }

  @Autowired

  @Override
  public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
    if (correct) {
      this.scoreCardRepository.save(new ScoreCard(userId, attemptId));
    }
    final Badge badgeToGive = Badge.FIRST_WON;

    final int totalScoreForUser = this.scoreCardRepository.getTotalScoreForUser(userId);

    final List<BadgeCard> badgeCardList = calculatesBadgesByScores(userId,
        totalScoreForUser);

    return new GameStats(userId,
        totalScoreForUser,
        badgeCardList.stream()
            .map(BadgeCard::getBadge).collect(
            Collectors.toList()));
  }

  /**
   * Calculates the {@link BadgeCard}s in order of its {@link ScoreCard}.
   * <p>
   * If any {@link Badge} has already been added previously, it aren't going to be added.
   * </p>
   *
   * @param userId            the user's id
   * @param totalScoreForUser the total score of the given user
   * @return a {@link BadgeCard} list with all {@link Badge} that have the given user.
   */
  private List<BadgeCard> calculatesBadgesByScores(Long userId,
      int totalScoreForUser) {
    final var scoreCardList = this.scoreCardRepository
        .findByUserIdOrderByScoreTimestampDesc(userId);
    final var badgeCardList = this.badgeCardRepository
        .findByUserIdOrderByBadgeTimestampDesc(userId);

    if (isFirstWon(scoreCardList, badgeCardList)) {
      badgeCardList.add(storesBadgeCard(userId, Badge.FIRST_WON));
    }

    if (totalScoreForUser > 99) {
      badgeCardList.add(storesBadgeCard(userId, Badge.BRONZE_MULTIPLICATOR));
    }

    return badgeCardList;
  }

  /**
   * Stores a {@link BadgeCard} created from the fiven {@code userId} and {@code badgeToAdd}
   *
   * @param userId     the user's id
   * @param badgeToAdd the {@link Badge} to be added.
   * @return the {@link BadgeCard} stored;
   */
  private BadgeCard storesBadgeCard(Long userId, Badge badgeToAdd) {
    BadgeCard badgeCardToGive = new BadgeCard(userId, badgeToAdd);
    this.badgeCardRepository.save(badgeCardToGive);
    return badgeCardToGive;
  }

  /**
   * Determine whether the FIRST_WON {@link Badge} has been added or not.
   *
   * @param scoreCardList the current {@link ScoreCard} list.
   * @param badgeCardList the current {@link BadgeCard} list.
   * @return true if it is the first time the user wins.
   */
  private boolean isFirstWon(List<ScoreCard> scoreCardList, List<BadgeCard> badgeCardList) {
    return scoreCardList.size() == 1 && badgeCardList.stream()
        .noneMatch(badge -> badge.getBadge().equals(Badge.FIRST_WON));
  }


  @Override
  public GameStats retrieveStatsForUser(Long userId) {
    return null;
  }
}
