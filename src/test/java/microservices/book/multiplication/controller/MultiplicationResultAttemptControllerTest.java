package microservices.book.multiplication.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.controller.MultiplicationResultAttemptController.ResultResponse;
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
  private JacksonTester<ResultResponse> jsonResponse;

  @BeforeEach
  void setup() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given a correct multiplication attempt, when it is checked, then returns true ")
  void postResultReturnCorrect() throws Exception {
    genericParameterizedTest(true);
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given an incorrect multiplication attempt, when it is checked, then returns false ")
  void postResultReturnNotCorrect() throws Exception {
    genericParameterizedTest(false);
  }

  private void genericParameterizedTest(final boolean correct) throws Exception {
    // given (remember we're not testing here the service itself)

    given(multiplicationService
        .checkAttempt(any(MultiplicationResultAttempt.class)))
        .willReturn(correct);

    User user = new User("John");
    Multiplication multiplication = new Multiplication(50, 70);
    MultiplicationResultAttempt attempt = new MultiplicationResultAttempt(user, multiplication,
        3500);

    // when
    MockHttpServletResponse response = mvc.perform(post("/results")
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonResult.write(attempt)
            .getJson()))
        .andReturn().getResponse();

    // then
    assertThat(response.getContentAsString())
        .isEqualTo(jsonResponse.write(new ResultResponse(correct)).getJson());
  }
}