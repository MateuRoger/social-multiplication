package microservices.book.gamification.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
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
  @DisplayName("Given an existing user and an incorrect attempt, when processing the attempt, then the user has the same score than before")
  void givenExistingUserAndInCorrectAttempt_whenProcessing_thenUserDoesNotIncreaseScore() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;

    final GameStats expectedGameStats = new GameStats(userId, 10, Lists.emptyList());

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(10);

    // when
    final GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, false);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and a correct attempt, when processing the attempt, then the user has 10 points more than before")
  void givenExistingUserAndCorrectAttempt_whenProcessing_thenUserIncreasesScore() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;
    final GameStats expectedGameStats = new GameStats(userId, 20, Lists.emptyList());

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(10);

    // when
    final GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and a correct attempt, when processing the attempt, then new score is stored")
  void givenExistingUserAndCorrectAttempt_whenProcessing_thenNewScoreIsStored() {
    // given
    final long userId = 1L;
    final long attemptId = 10L;
    final ScoreCard expectedScoreCard = new ScoreCard(userId, attemptId);

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(10);

    // when
    this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    verify(scoreCardRepository).getTotalScoreForUser(userId);
    verify(scoreCardRepository).save(expectedScoreCard);
  }
}