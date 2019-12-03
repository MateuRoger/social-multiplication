package microservices.book;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;

public class LeaderBoardFeatureSteps {

  private final MultiplicationFeatureSteps mSteps;

  public LeaderBoardFeatureSteps() {
    this.mSteps = new MultiplicationFeatureSteps();
  }

  @Then("the user ([^\\s]+) is the number (\\d+) on the leaderboard")
  public void the_user_is_the_number_on_the_leaderboard(final String userAlias, final int position) {
    assertThat(userAlias)
        .isEqualTo(
            mSteps.getApp().getUser(
                mSteps.getApp().getLeaderBoard().get(position - 1).getUserId()
            ).getAlias());
  }
}
