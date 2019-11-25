package microservices.book.gamification.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Calendar;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.ScoreCardRepository;
import microservices.book.gamification.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ScoreController.class)
class ScoreControllerTest {

  @MockBean
  private GameService gameService;

  private JacksonTester<ScoreCard> json;

  @Autowired
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given an existing attempt id, When get score for attempt, then returns the related score card ")
  void givenExistingAttemptId_whenGetScoreForAttempt_thenReturnsScoreCardOfUser() throws Exception {
    // given
    final long attemptId = 10L;
    final ScoreCard expectedScoreCard = new ScoreCard(100L, 1L, attemptId, Calendar.getInstance().getTimeInMillis(), ScoreCard.DEFAULT_SCORE);

    given(this.gameService.getScoreForAttempt(attemptId)).willReturn(expectedScoreCard);

    // when
    final MockHttpServletResponse response = mvc.perform(get("/scores/" + attemptId).accept(MediaType.APPLICATION_JSON)).andReturn()
        .getResponse();

    // then
    assertThat(response.getContentAsString()).isEqualTo(json.write(expectedScoreCard).getJson());
  }
}