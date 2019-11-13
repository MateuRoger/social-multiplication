package microservices.book.multiplication.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.domain.Multiplication;
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

@WebMvcTest(MultiplicationController.class)
class MultiplicationControllerTest {

  @MockBean
  private MultiplicationService multiplicationService;

  @Autowired
  private MockMvc mvc;

  // This object will be magically initialized by the initFields methods below,
  private JacksonTester<Multiplication> json;

  @BeforeEach
  void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("When someone asks for a random multiplication, then returns a Json with the random multiplication")
  void getRandomMultiplicationTest() throws Exception {
    // given
    given(multiplicationService.createRandomMultiplication())
        .willReturn(new Multiplication(70, 20));

    //When
    final MockHttpServletResponse response = mvc.perform(
        get("/multiplications/random")
            .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    //Then
    assertThat(response.getContentAsString())
        .isEqualTo(json.write(new Multiplication(70, 20))
            .getJson());
  }

}