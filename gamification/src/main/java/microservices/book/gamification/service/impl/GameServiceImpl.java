package microservices.book.gamification.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.business.badge.BadgeOperationExecutor;
import microservices.book.gamification.business.badge.criteria.impl.FirstWonBadgeCriteria;
import microservices.book.gamification.business.badge.criteria.impl.LuckyNumberCriteria;
import microservices.book.gamification.business.badge.criteria.impl.ScoredBasedBadgeCriteria;
import microservices.book.gamification.business.badge.impl.ObtainBadgeOperation;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.GameService;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;
  private final MultiplicationResultAttemptClient resultAttemptClient;

  /**
   * Constructor
   *
   * @param scoreCardRepository the injected {@link ScoreCardRepository}.
   * @param badgeCardRepository the injected {@link BadgeCardRepository}.
   * @param resultAttemptClient the injected {@link MultiplicationResultAttemptClient}.
   */
  GameServiceImpl(final ScoreCardRepository scoreCardRepository, final BadgeCardRepository badgeCardRepository,
      final MultiplicationResultAttemptClient resultAttemptClient) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
    this.resultAttemptClient = resultAttemptClient;
  }

  @Override
  public GameStats newAttemptForUser(final Long userId, final Long attemptId, final boolean correct) {
    ScoreCard scoredCard = new ScoreCard();

    if (correct) {
      scoredCard = new ScoreCard(userId, attemptId);
      this.scoreCardRepository.save(scoredCard);
    }
    log.info("User with id {} scored {} points for attempt id {}", userId, scoredCard.getScore(), attemptId);

    final int totalScore = Optional.ofNullable(this.scoreCardRepository.getTotalScoreForUser(userId)).orElse(0);
    log.info("New score for user {} is {}", userId, totalScore);

    final List<BadgeCard> badgeCardList = processNewBadgesByCase(userId, totalScore, attemptId);

    return new GameStats(userId, totalScore,
        badgeCardList.stream().map(BadgeCard::getBadge).collect(Collectors.toList()));
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
   * Process all new {@link BadgeCard} obtained according to each case. If there is any new, it will be stored in the
   * database
   *
   * @param userId       the user Id
   * @param currentScore the user's current score
   * @param attemptId    the {@link MultiplicationResultAttempt}'s id
   * @return a list of all the new {@link BadgeCard} that have been stored.
   */
  private List<BadgeCard> processNewBadgesByCase(final Long userId, final int currentScore, final Long attemptId) {
    final var scoreCardList = this.scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId);
    final var badgeCardList = this.badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);

    final BadgeOperationExecutor badgeOptExecutor = getBadgeOperationExecutor(currentScore, scoreCardList,
        badgeCardList, attemptId);

    final Set<Badge> newBadges = badgeOptExecutor.executeAllOperations();

    final List<BadgeCard> allBadges = new ArrayList<>(List.copyOf(badgeCardList));
    allBadges.addAll(newBadges.stream()
        .map(badge -> giveBadgeCard(userId, badge))
        .collect(Collectors.toList()));

    return allBadges;
  }

  /**
   * Gets a {@link BadgeOperationExecutor} that will be executes all the {@link ObtainBadgeOperation} for all cases
   *
   * @param currentScore  the user's current score.
   * @param scoreCardList the current {@link ScoreCard} list of the user.
   * @param badgeCardList the current {@link BadgeCard} list of the user.
   * @param attemptId     the {@link MultiplicationResultAttempt}'s id
   * @return a {@link BadgeOperationExecutor}
   */
  private BadgeOperationExecutor getBadgeOperationExecutor(final int currentScore, final List<ScoreCard> scoreCardList,
      final List<BadgeCard> badgeCardList, final Long attemptId) {
    final MultiplicationResultAttempt attempt = this.resultAttemptClient
        .retrieveMultiplicationResultAttemptById(attemptId);

    final BadgeOperationExecutor badgeOptExecutor = new BadgeOperationExecutor();

    badgeOptExecutor.addBadgeOperation(
        new ObtainBadgeOperation(new FirstWonBadgeCriteria(scoreCardList, badgeCardList, Badge.FIRST_WON)));

    badgeOptExecutor.addBadgeOperation(new ObtainBadgeOperation(
        new ScoredBasedBadgeCriteria(badgeCardList, 100, currentScore, Badge.BRONZE_MULTIPLICATOR)));
    badgeOptExecutor.addBadgeOperation(new ObtainBadgeOperation(
        new ScoredBasedBadgeCriteria(badgeCardList, 500, currentScore, Badge.SILVER_MULTIPLICATOR)));
    badgeOptExecutor.addBadgeOperation(new ObtainBadgeOperation(
        new ScoredBasedBadgeCriteria(badgeCardList, 999, currentScore, Badge.GOLD_MULTIPLICATOR)));

    badgeOptExecutor.addBadgeOperation(
        new ObtainBadgeOperation(new LuckyNumberCriteria(badgeCardList, attempt, Badge.LUCKY_NUMBER)));

    return badgeOptExecutor;
  }


  @Override
  public GameStats retrieveStatsForUser(final Long userId) {
    return new GameStats(userId,
        Optional.ofNullable(this.scoreCardRepository.getTotalScoreForUser(userId)).orElse(0),
        this.badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId).stream()
            .map(BadgeCard::getBadge)
            .collect(Collectors.toList()));
  }

  @Override
  public ScoreCard getScoreForAttempt(final long attemptId) {
    final ScoreCard byAttempId = this.scoreCardRepository.findByAttemptId(attemptId);
    return byAttempId;
  }
}
