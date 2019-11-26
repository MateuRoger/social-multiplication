package microservices.book.gamification.service.impl;

import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  private final BadgeCardRepository badgeCardRepository;
  private final ScoreCardRepository scoreCardRepository;

  @Autowired
  public AdminServiceImpl(BadgeCardRepository badgeCardRepository,
      ScoreCardRepository scoreCardRepository) {
    this.badgeCardRepository = badgeCardRepository;
    this.scoreCardRepository = scoreCardRepository;
  }

  @Override
  public void deleteDataBaseContents() {
    this.badgeCardRepository.deleteAll();
    this.scoreCardRepository.deleteAll();
  }
}
