package microservices.book.multiplication.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.repository.UserRepository;
import microservices.book.multiplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


class UserServiceImplTest {

  private static UserService userService;

  @Mock
  private static UserRepository userRepositoryMocked;

  @BeforeEach
  void setUp() {
    // With this call to initMocks we tell Mockito to process the annotations
    MockitoAnnotations.initMocks(this);
    userService = new UserServiceImpl(userRepositoryMocked);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a correct user id, when getUserById, then returns the corresponding user")
  void CheckReturnsCorrectUserById() {
    // given
    final long userId = 10L;
    final User expectedUser = new User("Test");

    given(userRepositoryMocked.findById(userId)).willReturn(Optional.of(expectedUser));

    // when
    final User obtainedUser = userService.getUserById(userId);

    // then
    assertThat(obtainedUser).isEqualTo(expectedUser);
  }
}