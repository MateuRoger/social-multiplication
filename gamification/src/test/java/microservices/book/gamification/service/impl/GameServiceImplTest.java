package microservices.book.gamification.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import microservices.book.gamification.client.MultiplicationResultAttemptClient;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class GameServiceImplTest {

  @Mock
  private BadgeCardRepository badgeCardRepository;

  @Mock
  private ScoreCardRepository scoreCardRepository;

  @Mock
  private MultiplicationResultAttemptClient resultAttemptClient;


  private GameServiceImpl gameService;

  @BeforeEach
  void setUp() {
    // With this call to initMocks we tell Mockito to process the annotations
    MockitoAnnotations.initMocks(this);
    this.gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository, resultAttemptClient);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and an incorrect attempt, when new attempt for user, then the user has the same score than before and store it")
  void givenExistingUserAndInCorrectAttempt_whenNewAttemptForUser_thenUserDoesNotIncreaseScoreAndStoreIt() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;

    final GameStats expectedGameStats = new GameStats(userId, 10, Lists.emptyList());

    givenCommonForNewAttempt(userId, attemptId, 10, new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    final GameStats obtainedGameStats = this.gameService
        .newAttemptForUser(userId, attemptId, false);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
    final ArgumentCaptor<ScoreCard> scoreCardCaptor = ArgumentCaptor.forClass(ScoreCard.class);
    verify(scoreCardRepository, never()).save(scoreCardCaptor.capture());
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and a correct attempt, when new attempt for user, then the user has 10 points more than before")
  void givenExistingUserAndCorrectAttempt_whenNewAttemptForUser_thenUserIncreasesScore() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;
    final GameStats expectedGameStats = new GameStats(userId, 20, Lists.emptyList());

    givenCommonForNewAttempt(userId, attemptId, 20, new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    final GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and a correct attempt, when new attempt for user, then new score is stored")
  void givenExistingUserAndCorrectAttempt_whenNewAttemptForUser_thenNewScoreIsStored() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;
    final ScoreCard expectedScoreCard = new ScoreCard(userId, attemptId);

    givenCommonForNewAttempt(userId, attemptId, 20, new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    final ArgumentCaptor<ScoreCard> scoreCardCaptor = ArgumentCaptor.forClass(ScoreCard.class);
    verify(scoreCardRepository).save(scoreCardCaptor.capture());
    assertThat(scoreCardCaptor.getValue().getScore()).isEqualTo(expectedScoreCard.getScore());
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a new user and a correct attempt, when new attempt for user, then receives the FIRST_WON badge")
  void givenExistingUserAndIncorrectAttempt_whenNewAttemptForUser_thenReceiveFIRST_WONBadge() {
    // given
    final long userId = 2L;
    final long attemptId = 20L;
    final int currentScore = 10;

    final Badge expectedBadge = Badge.FIRST_WON;

    givenCommonForCalculationBadges(Collections.emptyList(), currentScore,
        List.of(new BadgeCard(userId, expectedBadge)), userId, attemptId,
        new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    final GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(new GameStats(userId, currentScore, List.of(expectedBadge)));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a user having 100 points but doesn't have the BRONZE_MULTIPLICATOR badge, when new attempt for user, then receives the BRONZE_MULTIPLICATOR badge")
  void givenUserHaving100PointsButNotBRONZE_MULTIPLICATOR_whenNewAttemptForUser_thenReceiveBRONZE_MULTIPLICATOR() {
    // given
    final long userId = 3L;
    final long attemptId = 30L;
    final List<Badge> currentBadgeList = List.of(Badge.FIRST_WON);

    final BadgeCard expectedNewBadgeCard = new BadgeCard(userId, Badge.BRONZE_MULTIPLICATOR);
    final int currentScore = 100;
    givenCommonForCalculationBadges(currentBadgeList, currentScore, List.of(expectedNewBadgeCard), userId, attemptId,
        new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    final GameStats obtainedGameStats = gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(new GameStats(userId, currentScore,
        List.of(expectedNewBadgeCard.getBadge())));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a user having 500 points but doesn't have the SILVER_MULTIPLICATOR badge, when new attempt for user, then receives the SILVER_MULTIPLICATOR badge")
  void givenUserHaving500PointsButNotSILVER_MULTIPLICATOR_whenNewAttemptForUser_thenReceiveSILVER_MULTIPLICATOR() {
    // given
    final long userId = 4L;
    final long attemptId = 40L;
    final List<Badge> currentBadgeList = List.of(Badge.FIRST_WON, Badge.BRONZE_MULTIPLICATOR);

    final BadgeCard expectedNewBadgeCard = new BadgeCard(userId, Badge.SILVER_MULTIPLICATOR);
    final int currentScore = 500;

    givenCommonForCalculationBadges(currentBadgeList, currentScore, List.of(expectedNewBadgeCard), userId, attemptId,
        new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    final GameStats obtainedGameStats = gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(new GameStats(userId, currentScore,
        List.of(expectedNewBadgeCard.getBadge())));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a user having 999 points but doesn't have the GOLD_MULTIPLICATOR badge, when new attempt for user, then receives the GOLD_MULTIPLICATOR badge")
  void givenUserHaving999PointsButNotGOLD_MULTIPLICATOR_whenNewAttemptForUser_thenReceiveGOLD_MULTIPLICATOR() {
    // given
    final long userId = 5L;
    final long attemptId = 50L;
    final List<Badge> currentBadgeList = List
        .of(Badge.FIRST_WON, Badge.BRONZE_MULTIPLICATOR, Badge.SILVER_MULTIPLICATOR);

    final BadgeCard expectedNewBadgeCard = new BadgeCard(userId, Badge.GOLD_MULTIPLICATOR);
    final int currentScore = 999;
    givenCommonForCalculationBadges(currentBadgeList, currentScore, List.of(expectedNewBadgeCard), userId, attemptId,
        new MultiplicationResultAttempt("john", 10, 10, 100, true));

    // when
    final GameStats obtainedGameStats = gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(new GameStats(userId, currentScore,
        List.of(expectedNewBadgeCard.getBadge())));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an attempt with one of the factor is 47, when process for badges, then we gets the LUCKY_NUMBER badge")
  void TestTheObtainingOfLuckyNumberBadge() {
    // given
    final long userId = 6L;
    final long attemptId = 60L;
    final List<Badge> currentBadgeList = Collections.emptyList();

    final int currentScore = 10;
    final List<BadgeCard> expectedBadgeCardList = List
        .of(new BadgeCard(userId, Badge.FIRST_WON), new BadgeCard(userId, Badge.LUCKY_NUMBER));

    givenCommonForCalculationBadges(currentBadgeList, currentScore,
        expectedBadgeCardList, userId, attemptId,
        new MultiplicationResultAttempt("john", 42, 10, 470, true));

    // when
    final GameStats obtainedGameStats = gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(new GameStats(userId, currentScore,
        expectedBadgeCardList.stream().map(BadgeCard::getBadge).collect(Collectors.toList())));
  }


  /**
   * Given common part for all test that verify obtaining the {@link BadgeCard} by {@link ScoreCard}.
   *
   * @param currentBadgeList      the current {@link Badge} list of the user.
   * @param currentScore          the current total score of the user.
   * @param expectedNewBadgeCards the list of expected new {@link BadgeCard} that the user must to obtain.
   * @param userId                the user id
   * @param attemptId             the {@link MultiplicationResultAttempt} id
   * @param resultAttempt         the given {@link MultiplicationResultAttempt}
   */
  private void givenCommonForCalculationBadges(final List<Badge> currentBadgeList, final int currentScore,
      final List<BadgeCard> expectedNewBadgeCards, final long userId, final long attemptId,
      final MultiplicationResultAttempt resultAttempt) {

    givenCommonForNewAttempt(userId, attemptId, currentScore, resultAttempt);

    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(List.of(new ScoreCard(userId, attemptId)));

    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(
            currentBadgeList.stream()
                .map(badge -> new BadgeCard(userId, badge))
                .collect(Collectors.toList()));

    given(resultAttemptClient.retrieveMultiplicationResultAttemptById(attemptId)).willReturn(
        resultAttempt);

    expectedNewBadgeCards.forEach(badgeCard -> given(badgeCardRepository.save(badgeCard)).willReturn(badgeCard));

  }

  /**
   * @param userId       the user id
   * @param attemptId    the {@link MultiplicationResultAttempt} id
   * @param currentScore the current total score of the user.
   * @param attempt      the given {@link MultiplicationResultAttempt}
   */
  private void givenCommonForNewAttempt(final long userId, final long attemptId,
      final int currentScore, final MultiplicationResultAttempt attempt) {
    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(currentScore);
    given(resultAttemptClient.retrieveMultiplicationResultAttemptById(attemptId)).willReturn(
        attempt);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a user with badgeCards and scoresCards, when retrieveStatsForUser, then returns a gameStats with all his stats")
  void givenUserWithBadgeCardsAndScoresCards_whenRetrieveStatsForUser_thenReturnsGameStatsWithAllStats() {
    // given
    final long userId = 123L;
    final int currentScore = 345;
    final List<BadgeCard> currentBadges = List
        .of(new BadgeCard(userId, Badge.FIRST_WON), new BadgeCard(userId, Badge.BRONZE_MULTIPLICATOR));

    given(this.scoreCardRepository.getTotalScoreForUser(userId)).willReturn(currentScore);
    given(this.badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(
        currentBadges);

    // when
    final GameStats currentGameStats = this.gameService.retrieveStatsForUser(userId);

    // then
    assertThat(currentGameStats)
        .isEqualTo(new GameStats(userId, currentScore,
            currentBadges.stream()
                .map(BadgeCard::getBadge).collect(Collectors.toList())));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an attempt id, when getScoreForAttempt, then returns the ScoreCard associated")
  void testRetriveAScoreCardByAttemptId() {
    // given 
    final long attemptId = 10L;
    final ScoreCard expectedScore = new ScoreCard(100L, 10L, attemptId, Calendar.getInstance().getTimeInMillis(),
        ScoreCard.DEFAULT_SCORE);

    given(this.scoreCardRepository.findByAttemptId(attemptId)).willReturn(expectedScore);

    // when
    final ScoreCard obtainedScore = this.gameService.getScoreForAttempt(attemptId);

    // then
    assertThat(obtainedScore).isEqualTo(
        expectedScore);
  }
}