package microservices.book.gamification.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.List;
import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.LeaderBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LeaderBoardServiceImplTest {

  @Mock
  private ScoreCardRepository scoreCardRepository;

  private LeaderBoardService leaderBoardService;

  @BeforeEach
  void setUp() {
    // With this call to initMocks we tell Mockito to process the annotations
    MockitoAnnotations.initMocks(this);
    this.leaderBoardService = new LeaderBoardServiceImpl(this.scoreCardRepository);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given users with score in the system, when gets top 10, then returns the top ten descending")
  void givenUsersWithScore_whenGetsTopTen_thenReturnsTopTenDescending() {
    // given
    final List<LeaderBoardRow> currentLeaderBoardRows = List
        .of(new LeaderBoardRow(1L, 100L), new LeaderBoardRow(2L, 90L),
            new LeaderBoardRow(3L, 80L), new LeaderBoardRow(4L, 70L),
            new LeaderBoardRow(5L, 60L), new LeaderBoardRow(6L, 50L),
            new LeaderBoardRow(7L, 40L), new LeaderBoardRow(8L, 30L),
            new LeaderBoardRow(9L, 20L), new LeaderBoardRow(10L, 10L));
    given(scoreCardRepository.findFirst10()).willReturn(
        currentLeaderBoardRows);

    // when
    final List<LeaderBoardRow> obtainedTopTen = this.leaderBoardService.getCurrentLeaderBoard();

    // then
    assertThat(obtainedTopTen).containsAll(currentLeaderBoardRows);
  }
}