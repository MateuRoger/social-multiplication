package microservices.book;


import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.stream.IntStream;
import microservices.book.testutils.MultiplicationApplication;
import microservices.book.testutils.beans.AttemptResponse;
import microservices.book.testutils.beans.Stats;

public class MultiplicationFeatureSteps {

  private MultiplicationApplication app;
  private AttemptResponse lastAttemptResponse;
  private Stats lastStatsResponse;

  public MultiplicationFeatureSteps() {
    this.app = new MultiplicationApplication();
  }

  @Before
  public void cleanUp() throws InterruptedException {
    app.deleteData();
//    Thread.sleep(1000);
  }

  @When("^the user ([^\\s]+) sends (\\d+) ([^\\s]+) attempts$")
  public void the_user_sends_attempts(final String userAlias, final int attempts, final String rightOrWrong) {
    int attemptSent = IntStream.range(0, attempts)
        .mapToObj(i -> app.sendAttempt(userAlias, 10, 10, "right".equals(rightOrWrong) ? 100 : 258))
        .peek(response -> lastAttemptResponse = response)
        .mapToInt(response -> response.isCorrect() ? 1 : 0)
        .sum();

    assertThat(attemptSent)
        .withFailMessage("Error sending attempts to the application")
        .isEqualTo("right".equals(rightOrWrong) ? attempts : 0);
  }

  @Then("^the user gets a response indicating the attempt is ([^\\s]+)$")
  public void the_user_gets_a_response_indicating_the_attempt_is_(final String rightOrWrong) {
    assertThat(lastAttemptResponse.isCorrect())
        .withFailMessage("Expecting a response with a " + rightOrWrong + " attempt")
        .isEqualTo("right".equals(rightOrWrong));
  }

  @Then("^the user gets (\\d+) points for the attempt$")
  public void the_user_gets_points_for_the_attempt(final int points) throws InterruptedException {
    Thread.sleep(2000);
    assertThat(app.getScoreForAttempt(lastAttemptResponse.getId()).getScore())
        .isEqualTo(points);
  }

  @Then("^the user gets the ([^\\s]+) badge$")
  public void the_user_gets_the_type_badge(final String badgeType) throws InterruptedException {
    Thread.sleep(2000);
    lastStatsResponse = app.getStatsForUser(lastAttemptResponse.getUser().getId());
    List<String> userBadges = lastStatsResponse.getBadges();
    assertThat(userBadges).contains(badgeType);
  }

  @Then("^the user does not get any badge$")
  public void the_user_does_not_get_any_badge() {
    final Stats stats = app.getStatsForUser(lastAttemptResponse.getUser().getId());
    if (stats.getScore() == 0) {
      assertThat(stats.getBadges()).isNullOrEmpty();
    } else {
      assertThat(stats.getBadges()).isEqualTo(this.lastStatsResponse.getBadges());
    }
  }

  public MultiplicationApplication getApp() {
    return app;
  }

  public AttemptResponse getLastAttemptResponse() {
    return lastAttemptResponse;
  }

}
