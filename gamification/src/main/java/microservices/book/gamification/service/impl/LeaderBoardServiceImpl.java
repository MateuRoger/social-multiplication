package microservices.book.gamification.service.impl;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LeaderBoardServiceImpl implements LeaderBoardService {

  private final ScoreCardRepository scoreCardRepository;

  /**
   * Constructor.
   *
   * @param scoreCardRepository the injected {@link ScoreCardRepository}.
   */
  @Autowired
  LeaderBoardServiceImpl(final ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public List<LeaderBoardRow> getCurrentLeaderBoard() {
    log.info("Getting the current Leader Board");
    return this.scoreCardRepository.findFirst10();
  }
}
