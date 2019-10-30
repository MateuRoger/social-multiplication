package microservices.book.gamification.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import microservices.book.gamification.domain.GameStats;
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
    long userId = 1L;
    long attemptId = 10L;
    int currentScore = 10;
    GameStats expectedGameStats = new GameStats(userId, currentScore, Lists.emptyList());

    given(scoreCardRepository.getTotalScoreForUser(userId)).willReturn(currentScore);

    // when
    GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an existing user and a correct attempt, when processing the attempt, then the user has 10 points more than before")
  void givenExistingUserAndCorrectAttempt_whenProcessing_thenUserIncreaseScore() {
    // given
    long userId = 1L;
    long attemptId = 10L;
    GameStats expectedGameStats = new GameStats(userId, 20, Lists.emptyList());

    // when
    GameStats obtainedGameStats = this.gameService.newAttemptForUser(userId, attemptId, true);

    // then
    assertThat(obtainedGameStats).isEqualTo(expectedGameStats);
  }
}