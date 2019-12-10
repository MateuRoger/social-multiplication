package microservices.book.gamification.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import microservices.book.gamification.service.AdminService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
@ActiveProfiles(profiles = "test")
class AdminControllerEnabledTest {

  @MockBean
  private AdminService adminService;

  @Autowired
  private MockMvc mvc;

  @Test
  @Tag("API-Test")
  @DisplayName("Checks that the controller is working as expected when the profile is set to test")
  void deleteDataBaseTest() throws Exception {
    // when
    MockHttpServletResponse response = this.mvc
        .perform(post("/gamification/admin/delete-db").accept(MediaType.APPLICATION_JSON)).andReturn().getResponse();

    // then
    assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    verify(adminService).deleteDataBaseContents();
  }
}