package microservices.book.gamification.controller;

import java.util.List;
import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class implements a REST API for the Gamification LeaderBoard service.
 */
@RestController
@RequestMapping("/leaders")
public class LeaderBoardController {

  private final LeaderBoardService leaderBoardService;

  @Autowired
  public LeaderBoardController(LeaderBoardService leaderBoardService) {
    this.leaderBoardService = leaderBoardService;
  }

  @GetMapping
  public List<LeaderBoardRow> getLeaderBoard() {
    return this.leaderBoardService.getCurrentLeaderBoard();
  }
}
