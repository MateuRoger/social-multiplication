package microservices.book.gamification.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;//NOPMD

  public GameServiceImpl(final ScoreCardRepository scoreCardRepository, final BadgeCardRepository badgeCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
  }

  @Autowired

  @Override
  public GameStats newAttemptForUser(final Long userId, final Long attemptId, final boolean correct) {
    ScoreCard scoredCard = new ScoreCard();

    if (correct) {
      scoredCard = new ScoreCard(userId, attemptId);
      this.scoreCardRepository.save(scoredCard);
    }
    log.info("User with id {} scored {} points for attempt id {}", userId, scoredCard.getScore(), attemptId);

    final int totalScore = this.scoreCardRepository.getTotalScoreForUser(userId);
    log.info("New score for user {} is {}", userId, totalScore);

    final List<BadgeCard> badgeCardList = processForBadges(userId, totalScore);

    return new GameStats(userId, totalScore,
        badgeCardList.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
  }

  /**
   * Calculates all the {@link BadgeCard}s that have the user.
   *
   * @param userId            the user's id
   * @param totalScoreForUser the total score of the given user
   * @return a {@link BadgeCard} list with all {@link Badge} that have the given user.
   */
  private List<BadgeCard> processForBadges(final Long userId, final int totalScoreForUser) {
    final var scoreCardList = this.scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
    final var badgeCardList = this.badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

    badgeCardList.addAll(processFirstWonBadge(userId, scoreCardList, badgeCardList));
    badgeCardList.addAll(processScoreBasedOnScore(userId, totalScoreForUser, badgeCardList));

    return badgeCardList;
  }

  /**
   * Give a {@link BadgeCard} created from the given {@code userId} and {@code badgeToAdd} to the user.
   *
   * @param userId     the user's id
   * @param badgeToAdd the {@link Badge} to be added.
   * @return the {@link BadgeCard} stored;
   */
  private BadgeCard giveBadgeCard(final Long userId, final Badge badgeToAdd) {
    final BadgeCard newBadge = new BadgeCard(userId, badgeToAdd);
    final BadgeCard gaveBadgeCard = this.badgeCardRepository.save(newBadge);

    log.info("User with id {} won a new badge: {}", userId, newBadge);

    return gaveBadgeCard;
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
  private List<BadgeCard> processFirstWonBadge(final Long userId, final List<ScoreCard> scoreCardList,
      final List<BadgeCard> badgeCardList) {

    List<BadgeCard> newBadgeCards = Collections.emptyList();

    if (scoreCardList.size() == 1 && notContainsBadge(badgeCardList, Badge.FIRST_WON)) {
      newBadgeCards = List.of(giveBadgeCard(userId, Badge.FIRST_WON));
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
  private List<BadgeCard> processScoreBasedOnScore(final Long userId, final int currentScore,
      final List<BadgeCard> badgeCardList) {
    return Arrays.stream(Badge.values())
        .filter(badge -> badge.getMinScoreToGet() != null)
        .filter(badge -> notContainsBadge(badgeCardList, badge))
        .filter(badge -> currentScore >= badge.getMinScoreToGet())
        .map(badge -> giveBadgeCard(userId, badge))
        .collect(Collectors.toList());
  }

  /**
   * Determines if the given {@code badgeToCheck} exist or not into the given {@link BadgeCard} list.
   *
   * @param badgeCardList the current {@link BadgeCard} list of the user.
   * @param badgeToCheck  the {@link Badge} to be checked.
   * @return true if the given {@code badgeToCheck} does not exist into the given {@code badgeCardList}.
   */
  private boolean notContainsBadge(final List<BadgeCard> badgeCardList, final Badge badgeToCheck) {
    return badgeCardList.stream()
        .noneMatch(badge -> badge.getBadge().equals(badgeToCheck));
  }


  @Override
  public GameStats retrieveStatsForUser(final Long userId) {
    return null;
  }
}
