package microservices.book.gamification.service.impl;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
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
    log.info("Starting to delete all contents into the database");
    this.badgeCardRepository.deleteAll();
    this.scoreCardRepository.deleteAll();
  }
}
