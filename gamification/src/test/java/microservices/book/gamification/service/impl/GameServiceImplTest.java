package microservices.book.gamification.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
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

  private GameServiceImpl gameService;

  @BeforeEach
  void setUp() {
    // With this call to initMocks we tell Mockito to process the annotations
    MockitoAnnotations.initMocks(this);
    this.gameService = new GameServiceImpl(scoreCardRepository, badgeCardRepository);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and an incorrect attempt, when new attempt for user, then the user has the same score than before and store it")
  void givenExistingUserAndInCorrectAttempt_whenNewAttemptForUser_thenUserDoesNotIncreaseScoreAndStoreIt() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;

    final GameStats expectedGameStats = new GameStats(userId, 10, Lists.emptyList());

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(10);

    // when
    final GameStats obtainedGameStats = this.gameService
        .newAttemptForUser(userId, attemptId, false);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
    ArgumentCaptor<ScoreCard> scoreCardCaptor = ArgumentCaptor.forClass(ScoreCard.class);
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

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(20);

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

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(10);

    // when
    this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    ArgumentCaptor<ScoreCard> scoreCardCaptor = ArgumentCaptor.forClass(ScoreCard.class);
    verify(scoreCardRepository).save(scoreCardCaptor.capture());
    assertThat(scoreCardCaptor.getValue().getScore()).isEqualTo(expectedScoreCard.getScore());
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a new user and a correct attempt, when new attempt for user, then receives the FIRST_WON badge")
  void given_ExistingUserAndIncorrectAttempt_whenNewAttemptForUser_thenReceiveFIRST_WONBadge() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;
    final GameStats expectedGameStats = new GameStats(userId, 10, List.of(Badge.FIRST_WON));

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(10);

    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(List.of(new ScoreCard(userId, attemptId)));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(new ArrayList<>());

    // when
    GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a user having 100 points but doesn't have the BRONZE_MULTIPLICATOR badge, when new attempt for user, then receives the BRONZE_MULTIPLICATOR badge")
  void given_UserHaving100PointsButNotBRONZE_MULTIPLICATOR_whenNewAttemptForUser_thenReceiveBRONZE_MULTIPLICATOR() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;
    final GameStats expectedGameStats = new GameStats(userId, 10, List.of(Badge.FIRST_WON, Badge.BRONZE_MULTIPLICATOR));

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(100);

    given(scoreCardRepository.findByUserIdOrderByScoreTimestampDesc(userId))
        .willReturn(List.of(new ScoreCard(userId, attemptId)));
    given(badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId))
        .willReturn(List.of(new BadgeCard(userId, Badge.FIRST_WON)));

    // when
    GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }

}