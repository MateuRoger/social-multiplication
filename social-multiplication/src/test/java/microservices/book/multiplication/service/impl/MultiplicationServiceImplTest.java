package microservices.book.multiplication.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.event.EventDispatcher;
import microservices.book.multiplication.event.MultiplicationSolvedEvent;
import microservices.book.multiplication.repository.MultiplicationRepository;
import microservices.book.multiplication.repository.MultiplicationResultAttemptRepository;
import microservices.book.multiplication.repository.UserRepository;
import microservices.book.multiplication.service.RandomGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class MultiplicationServiceImplTest {

  @Mock
  private MultiplicationRepository multiplicationRepository;
  private MultiplicationServiceImpl multiplicationService;
  @Mock
  private MultiplicationResultAttemptRepository attemptRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private RandomGeneratorService randomGeneratorService;
  @Mock
  private EventDispatcher eventDispatcher;

  @BeforeEach
  void setUp() {
    // With this call to initMocks we tell Mockito to process the annotations
    MockitoAnnotations.initMocks(this);
    multiplicationService = new MultiplicationServiceImpl(randomGeneratorService, attemptRepository,
        userRepository, multiplicationRepository, eventDispatcher);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a correct multiplication result attempt, when it is checks, then returns true")
  void givenCorrectMultiplicationResultAttempt_whenChecks_thenReturnsTrue() {
    // given
    final String johnDoeAlias = "John_doe";
    final Multiplication multiplication = new Multiplication(50, 60);
    final User user = new User(johnDoeAlias);
    final int resultAttempt = 3000;

    final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
        user, multiplication, resultAttempt, false);

    final MultiplicationResultAttempt verifiedAttempt = new MultiplicationResultAttempt(
        user, multiplication, resultAttempt, true);

    final MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
        attempt.getId(), user.getId(), true);

    given(userRepository.findByAlias(johnDoeAlias)).willReturn(Optional.empty());
    given(attemptRepository.save(verifiedAttempt)).willReturn(verifiedAttempt);

    //when
    final MultiplicationResultAttempt attemptResult = multiplicationService.checkAttempt(attempt);

    //then
    assertThat(attemptResult.isCorrect()).isTrue();
    verify(attemptRepository).save(verifiedAttempt);
    verify(eventDispatcher).send(eq(event));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given an incorrect multiplication result attempt, when it is checks, then returns false")
  void givenIncorrectMultiplicationResultAttempt_whenChecks_thenReturnsFalse() {
    // given
    final String johnDoeAlias = "John_doe";
    final Multiplication multiplication = new Multiplication(50, 60);
    final User user = new User(johnDoeAlias);
    final int resultAttempt = 3010;

    final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
        user, multiplication, resultAttempt, false);

    final MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
        attempt.getId(), user.getId(), false);

    given(userRepository.findByAlias(johnDoeAlias)).willReturn(Optional.empty());
    given(attemptRepository.save(attempt)).willReturn(attempt);

    //when
    final MultiplicationResultAttempt attemptResult = multiplicationService.checkAttempt(attempt);

    //then
    assertThat(attemptResult.isCorrect()).isFalse();
    verify(attemptRepository).save(attempt);
    verify(eventDispatcher).send(eq(event));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given the same multiplication twice, when checks the attempt, then the stored multiplication is used")
  void givenSameMultiplicationTwice_whenChecks_themUsedStoredMultiplication()
      throws NoSuchFieldException, IllegalAccessException {
    // given
    final String johnDoeAlias = "John_doe";
    final int factorA = 50;
    final int factorB = 60;
    final Multiplication multiplication = new Multiplication(factorA, factorB);
    final Multiplication existingMultiplication = getStoredMultiplication();
    final User user = new User(johnDoeAlias);

    final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
        user, multiplication, 3000, false);
    final MultiplicationResultAttempt expectedCorrectAttempt = new MultiplicationResultAttempt(
        user, existingMultiplication, 3000, true);
    final MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
        attempt.getId(), user.getId(), true);

    given(multiplicationRepository.findByFactorAAndFactorB(factorA, factorB)).willReturn(
        Optional.of(existingMultiplication));

    // when
    multiplicationService.checkAttempt(attempt);

    // then
    verify(attemptRepository).save(expectedCorrectAttempt);
    verify(eventDispatcher).send(eq(event));
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given the a multiplication for first time, when checks the attempt, then the new multiplication is used")
  void givenSameMultiplicationFirstTime_whenChecks_themNewMultiplicationIsUsed() {
    // given
    final String johnDoeAlias = "John_doe";
    final int factor50 = 50;
    final int factor60 = 60;
    final Multiplication multiplication = new Multiplication(factor50, factor60);
    final User user = new User(johnDoeAlias);

    final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
        user, multiplication, 3000, false);
    final MultiplicationResultAttempt expectedCorrectAttempt = new MultiplicationResultAttempt(
        user, multiplication, 3000, true);
    final MultiplicationSolvedEvent event = new MultiplicationSolvedEvent(
        attempt.getId(), user.getId(), true);

    given(multiplicationRepository.findByFactorAAndFactorB(factor50, factor60)).willReturn(
        Optional.empty());

    // when
    multiplicationService.checkAttempt(attempt);

    // then
    verify(attemptRepository).save(expectedCorrectAttempt);
    verify(eventDispatcher).send(eq(event));
  }

  private Multiplication getStoredMultiplication()
      throws NoSuchFieldException, IllegalAccessException {
    final Multiplication existingMultiplication = new Multiplication(50, 60);
    final Field idField = existingMultiplication.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(existingMultiplication, (long) 1111);

    return existingMultiplication;
  }

  @Test
  @Tag("Unit")
  @DisplayName("Given a user with previous attempts, when retrieves he's stats, then returns top 5 attempts")
  void givenUserWithPreviousAttempts_whenRetrievesStats_thenReturnsTop5() {
    // given
    final String johnDoeAlias = "John_doe";
    final List<MultiplicationResultAttempt> latestAttempts = generateTop5Attempts(
    );

    // User don't exist previously
    given(userRepository.findByAlias(johnDoeAlias)).willReturn(Optional.empty());
    given(attemptRepository.findTop5ByUserAliasOrderByIdDesc(johnDoeAlias))
        .willReturn(latestAttempts);

    // when
    final List<MultiplicationResultAttempt> latestAttemptResult = this.multiplicationService
        .getStatsForUser(johnDoeAlias);

    // then
    assertThat(latestAttemptResult).isEqualTo(latestAttempts);

  }

  private List<MultiplicationResultAttempt> generateTop5Attempts() {
    final Multiplication multiplication = new Multiplication(50, 60);
    final User user = new User("John_doe");

    final MultiplicationResultAttempt attempt1 = new MultiplicationResultAttempt(
        user, multiplication, 3010, false);

    final MultiplicationResultAttempt attempt2 = new MultiplicationResultAttempt(
        user, multiplication, 3051, false);

    return List.of(attempt1, attempt2);
  }

  @Test
  @Tag("Unit")
  @DisplayName("Retrieves a MultiplicationResultAttempt by its id")
  void retrievesMultiplicationResultAttemptByItsId() {
    // given
    final MultiplicationResultAttempt desiredResult = new MultiplicationResultAttempt(
        new User("john"),
        new Multiplication(10, 10), 100, true);

    given(this.attemptRepository.findById(10L)).willReturn(Optional.of(desiredResult));

    //When
    final MultiplicationResultAttempt obtainedResult = this.multiplicationService.getResultById(10L);

    //Then
    assertThat(obtainedResult)
        .isEqualTo(desiredResult);
  }

}