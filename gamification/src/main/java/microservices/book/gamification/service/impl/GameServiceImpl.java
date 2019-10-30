package microservices.book.gamification.service.impl;

import java.util.stream.Collectors;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;

public class GameServiceImpl implements GameService {

  private final ScoreCardRepository scoreCardRepository;
  private final BadgeCardRepository badgeCardRepository;

  public GameServiceImpl(ScoreCardRepository scoreCardRepository,
      BadgeCardRepository badgeCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
    this.badgeCardRepository = badgeCardRepository;
  }

  @Autowired

  @Override
  public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {
    return new GameStats(userId, this.scoreCardRepository.getTotalScoreForUser(userId),
        this.badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId).stream()
            .map(BadgeCard::getBadge).collect(
            Collectors.toList()));
  }

  @Override
  public GameStats retrieveStatsForUser(Long userId) {
    return null;
  }
}
