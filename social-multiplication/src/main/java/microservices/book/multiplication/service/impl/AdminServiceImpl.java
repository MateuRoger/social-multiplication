package microservices.book.multiplication.service.impl;

import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import microservices.book.multiplication.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

  private final MultiplicationRepository multiplicationRepository;
  private final MultiplicationResultAttemptRepository resultAttemptRepository;
  private final UserRepository userRepository;

  @Autowired
  public AdminServiceImpl(MultiplicationRepository multiplicationRepository,
      MultiplicationResultAttemptRepository resultAttemptRepository,
      UserRepository userRepository) {
    this.multiplicationRepository = multiplicationRepository;
    this.resultAttemptRepository = resultAttemptRepository;
    this.userRepository = userRepository;
  }

  @Override
  public void deleteDataBaseContents() {
    this.multiplicationRepository.deleteAll();
    this.resultAttemptRepository.deleteAll();
    this.userRepository.deleteAll();
  }
}
