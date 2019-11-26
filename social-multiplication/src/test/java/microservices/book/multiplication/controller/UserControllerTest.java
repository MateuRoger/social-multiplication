package microservices.book.multiplication.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservices.book.multiplication.domain.User;
import microservices.book.multiplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

  @MockBean
  private UserService userService;

  private JacksonTester<User> json;

  @Autowired
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("Given a user id, when getUserById, then returns the corresponding user")
  void TestReturnsCorrectUserWhenRequestByUserId() throws Exception {
    // given
    final long userId = 10L;
    final User expectedUser = new User("Test");
    given(this.userService.getUserById(userId)).willReturn(expectedUser);

    // when
    final MockHttpServletResponse response = this.mvc.perform(get("/users/" + userId)).andReturn().getResponse();

    // then
    assertThat(response.getContentAsString()).isEqualTo(json.write(expectedUser).getJson());
  }
}