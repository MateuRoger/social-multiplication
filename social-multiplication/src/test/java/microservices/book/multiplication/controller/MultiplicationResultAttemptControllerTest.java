package microservices.book.multiplication.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import microservices.book.multiplication.domain.Multiplication;
import microservices.book.multiplication.domain.MultiplicationResultAttempt;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.MultiplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MultiplicationResultAttemptController.class)
class MultiplicationResultAttemptControllerTest {

  @MockBean
  private MultiplicationService multiplicationService;

  @Autowired
  private MockMvc mvc;

  // This object will be magically initiated by the initFields method bellow.
  private JacksonTester<MultiplicationResultAttempt> jsonResult;

  private JacksonTester<List<MultiplicationResultAttempt>> jsonResultAttemptList;

  @BeforeEach
  void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given a correct multiplication attempt, when it is checked, then returns true ")
  void givenCorrectMultiplicationAttempt_whenChecksIt_thenReturnsTrue() throws Exception {//NOPMD
    genericParameterizedTest(true);
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given an incorrect multiplication attempt, when it is checked, then returns false ")
  void givenInCorrectMultiplicationAttempt_whenChecksIt_thenReturnsFalse() throws Exception {//NOPMD
    genericParameterizedTest(false);
  }

  private void genericParameterizedTest(final boolean correct) throws Exception {
    // given (remember we're not testing here the service itself)

    final User johnUser = new User("John");
    final Multiplication multiplication = new Multiplication(50, 70);
    final int resultAttempt = 3500;

    given(multiplicationService
        .checkAttempt(any(MultiplicationResultAttempt.class)))
        .willReturn(new MultiplicationResultAttempt(
            johnUser, multiplication, resultAttempt, correct));

    final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
        johnUser, multiplication, resultAttempt, false);

    // when
    final MockHttpServletResponse response = mvc.perform(post("/results")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonResult.write(attempt)
            .getJson()))
        .andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(response.getContentAsString()).isEqualTo(
        jsonResult.write(
            new MultiplicationResultAttempt(attempt.
                getUser(),
                attempt.getMultiplication(),
                attempt.getResultAttempt(),
                correct)
        ).getJson());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given an user alias, when gets his statistics, then returns his top 5 attempts")
  void givenUserAlias_whenGetsStatistics_thenReturnsTop5Attempts() throws Exception {
    // given
    final String johnDoeAlias = "john_doe";
    final User user = new User(johnDoeAlias);
    final int factor50 = 50;
    final int factor70 = 70;
    final Multiplication multiplication = new Multiplication(factor50, factor70);
    final MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(
        user, multiplication, 3500, false);

    final List<MultiplicationResultAttempt> recentAttempts = List.of(attempt, attempt);
    given(multiplicationService.getStatsForUser(johnDoeAlias)).willReturn(recentAttempts);

    // when
    final MockHttpServletResponse response = mvc.perform(
        get("/results")
            .param("alias", johnDoeAlias))
        .andReturn().getResponse();

    // then
    assertThat(response.getContentAsString()).isEqualTo(
        jsonResultAttemptList.write(
            recentAttempts
        ).getJson());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Retrieves a MultiplicationResultAttempt by its id")
  void retrievesMultiplicationResultAttemptByItsId() throws Exception {
    // given
    final MultiplicationResultAttempt desiredResult = new MultiplicationResultAttempt(
        new User("john"),
        new Multiplication(10, 10), 100, true);

    given(this.multiplicationService.getResultById(10L)).willReturn(desiredResult);

    //When
    final MockHttpServletResponse response = mvc.perform(
        get("/results/10").accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    //Then
    assertThat(response.getContentAsString()).isEqualTo(jsonResult.write(desiredResult).getJson());
  }
}