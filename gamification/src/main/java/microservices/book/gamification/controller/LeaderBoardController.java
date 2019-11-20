package microservices.book.gamification.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class implements a REST API for the Gamification LeaderBoard service.
 */
@Slf4j
@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {

  private final LeaderBoardService leaderBoardService;
  private final int serverPort;

  /**
   * Constructor
   *
   * @param leaderBoardService the {@link LeaderBoardService} injected.
   */
  @Autowired
  public LeaderBoardController(final LeaderBoardService leaderBoardService,
      @Value("${server.port}") final int serverPort) {
    this.leaderBoardService = leaderBoardService;
    this.serverPort = serverPort;
  }

  @GetMapping
  public List<LeaderBoardRow> getLeaderBoard() {
    log.info("Getting the leaders board from server @ {}", serverPort);
    return this.leaderBoardService.getCurrentLeaderBoard();
  }
}
