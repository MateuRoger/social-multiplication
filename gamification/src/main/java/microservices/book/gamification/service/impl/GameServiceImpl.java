package microservices.book.gamification.service.impl;

import java.util.Collections;
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

    final List<BadgeCard> badgeCardList = calculatesBadges(userId,
        totalScoreForUser);

    return new GameStats(userId,
        totalScoreForUser,
        badgeCardList.stream()
            .map(BadgeCard::getBadge).collect(
            Collectors.toList()));
  }

  /**
   * Calculates all the {@link BadgeCard}s that have the user.
   *
   * @param userId            the user's id
   * @param totalScoreForUser the total score of the given user
   * @return a {@link BadgeCard} list with all {@link Badge} that have the given user.
   */
  private List<BadgeCard> calculatesBadges(Long userId,
      int totalScoreForUser) {
    final var scoreCardList = this.scoreCardRepository
        .findByUserIdOrderByScoreTimestampDesc(userId);
    final var badgeCardList = this.badgeCardRepository
        .findByUserIdOrderByBadgeTimestampDesc(userId);

    badgeCardList.addAll(processFirstWonBadge(userId, scoreCardList, badgeCardList));
    badgeCardList.addAll(processScoreBasedBadges(userId, totalScoreForUser, badgeCardList));

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
   * Process the FIRST_WON {@link Badge}.
   * </p>
   * If the {@link Badge} is not added yet, It will be returned as a list of {@link BadgeCard}
   *
   * @param userId        the user Id
   * @param scoreCardList the current {@link ScoreCard} list.
   * @param badgeCardList the current {@link BadgeCard} list.
   * @return true if it is the first time the user wins.
   */
  private List<BadgeCard> processFirstWonBadge(Long userId, List<ScoreCard> scoreCardList,
      List<BadgeCard> badgeCardList) {

    List<BadgeCard> newBadgeCards = Collections.emptyList();

    if (scoreCardList.size() == 1 && notContainsBadge(badgeCardList, Badge.FIRST_WON)) {
      BadgeCard newBadge = storesBadgeCard(userId, Badge.FIRST_WON);
      newBadgeCards = List.of(newBadge);
    }

    return newBadgeCards;
  }

  /**
   * Process all {@link Badge} that are based on the score obtained.
   *
   * @param userId        the user id.
   * @param currentScore  the current score of the user.
   * @param badgeCardList the current {@link Badge} of the user.
   * @return a {@link BadgeCard} list with the new obtained {@link Badge}.
   */
  private List<BadgeCard> processScoreBasedBadges(Long userId, int currentScore,
      List<BadgeCard> badgeCardList) {

    return badgeCardList.stream()
        .map(BadgeCard::getBadge)
        .filter(badge -> notContainsBadge(badgeCardList, badge))
        .filter(badge -> badge.getMinScoreToGet() != null)
        .filter(badge -> currentScore >= badge.getMinScoreToGet())
        .map(badge -> storesBadgeCard(userId, badge))
        .collect(Collectors.toList());
  }

  /**
   * Determines if the given {@code badgeToCheck} exist or not into the given {@link BadgeCard}
   * list.
   *
   * @param badgeCardList the current {@link BadgeCard} list of the user.
   * @param badgeToCheck  the {@link Badge} to be checked.
   * @return true if the given {@code badgeToCheck} does not exist into the given {@code
   * badgeCardList}.
   */
  private boolean notContainsBadge(List<BadgeCard> badgeCardList, Badge badgeToCheck) {
    return badgeCardList.stream()
        .noneMatch(badge -> badge.getBadge().equals(badgeToCheck));
  }


  @Override
  public GameStats retrieveStatsForUser(Long userId) {
    return null;
  }
}
