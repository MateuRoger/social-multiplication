package microservices.book.gamification.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.service.LeaderBoardService;
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

@WebMvcTest(LeaderBoardController.class)
class LeaderBoardControllerTest {

  @MockBean
  private LeaderBoardService leaderBoardService;

  private JacksonTester<List<LeaderBoardRow>> json;

  @Autowired
  private MockMvc mvc;

  @BeforeEach
  void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  @Tag("API-Test")
  @DisplayName("when the top ten is requested, then returns a list of leaders board")
  void givenUser_whenAsksForStatistics_thenTheyAreReturned() throws Exception {
    // given
    final List<LeaderBoardRow> currentLeaderBoardRows = List
        .of(new LeaderBoardRow(1L, 100L), new LeaderBoardRow(2L, 90L),
            new LeaderBoardRow(3L, 80L), new LeaderBoardRow(4L, 70L),
            new LeaderBoardRow(5L, 60L), new LeaderBoardRow(6L, 50L),
            new LeaderBoardRow(7L, 40L), new LeaderBoardRow(8L, 30L),
            new LeaderBoardRow(9L, 20L), new LeaderBoardRow(10L, 10L));
    given(this.leaderBoardService.getCurrentLeaderBoard()).willReturn(currentLeaderBoardRows);

    // when
    final MockHttpServletResponse response = mvc.perform(get("/leaders").accept(MediaType.APPLICATION_JSON)).andReturn()
        .getResponse();

    // then
    assertThat(response.getContentAsString()).isEqualTo(json.write(currentLeaderBoardRows).getJson());
  }
}