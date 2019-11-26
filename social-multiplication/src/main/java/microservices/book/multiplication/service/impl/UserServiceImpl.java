package microservices.book.multiplication.service.impl;

import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.UserRepository;
import microservices.book.multiplication.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  /**
   * Constructed with injection
   *
   * @param userRepository the {@link UserRepository} to be injected;
   */
  public UserServiceImpl(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User getUserById(Long userId) {
    return this.userRepository.findById(userId).orElse(null);
  }
}
