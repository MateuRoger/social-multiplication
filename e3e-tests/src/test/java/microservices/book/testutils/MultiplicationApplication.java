package microservices.book.testutils;

import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.DeserializationFeature;
import io.cucumber.datatable.dependency.com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import microservices.book.testutils.beans.AttemptResponse;
import microservices.book.testutils.beans.LeaderBoardPosition;
import microservices.book.testutils.beans.ScoreResponse;
import microservices.book.testutils.beans.Stats;
import microservices.book.testutils.beans.User;
import microservices.book.testutils.http.ApplicationHttpUtils;

/**
 * Model the multiplication behavior
 */
public class MultiplicationApplication {

  private static final String APPLICATION_BASE_URL = "http://localhost:8000/api";
  private static final String CONTEXT_ATTEMPTS = "/results";
  private static final String CONTEXT_SCORE = "/scores/";
  private static final String CONTEXT_STATS = "/stats";
  private static final String CONTEXT_USERS = "/users/";
  private static final String CONTEXT_LEADERBOARD = "/leaders";
  private static final String CONTEXT_DELETE_DATA_GAM = "/gamification/admin/delete-db";
  private static final String CONTEXT_DELETE_DATA_MULT = "/multiplications/admin/delete-db";
  private static final String USER_ID_PARAM = "?userId=";
  private final ApplicationHttpUtils httpUtils;

  public MultiplicationApplication() {
    this.httpUtils = new ApplicationHttpUtils(APPLICATION_BASE_URL);
  }

  /**
   * Sends an Attempt to the system
   *
   * @param userAlias who is sending de attempt
   * @param factorA   the factor A of the multiplication
   * @param factorB   the factor B of the multiplication
   * @param result    the result of the multiplication
   * @return the {@link AttemptResponse} that the system returns.
   */
  public AttemptResponse sendAttempt(final String userAlias, final int factorA, final int factorB, final int result) {
    final String attemptJson =
        "{\"user\":{\"alias\":\"" + userAlias + "\"}," + "\"multiplication\":{\"factorA\":\"" + factorA
            + "\",\"factorB\":\"" + factorB + "\"}," + "\"resultAttempt\":\"" + result + "\"}";

    try {
      return new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(httpUtils.post(CONTEXT_ATTEMPTS, attemptJson), AttemptResponse.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves the Score for attempt from the system.
   *
   * @param attemptId the attempt id.
   * @return the {@link ScoreResponse} that the system returns.
   */
  public ScoreResponse getScoreForAttempt(final long attemptId) {
    final String response = httpUtils.get(CONTEXT_SCORE + attemptId);
    if (response.isEmpty()) {
      return new ScoreResponse(0);
    } else {
      try {
        return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .readValue(response, ScoreResponse.class);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Retrieves the Stats of a user from the system.
   *
   * @param userId the user id.
   * @return the {@link Stats} of the user.
   */
  public Stats getStatsForUser(final long userId) {
    try {
      return new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(httpUtils.get(CONTEXT_STATS + USER_ID_PARAM + userId), Stats.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Deletes all the contents of the database for each microservice.
   */
  public void deleteData() {
    httpUtils.post(CONTEXT_DELETE_DATA_GAM, "");
    httpUtils.post(CONTEXT_DELETE_DATA_MULT, "");
  }

  /**
   * Retrieves the Leader Board form the system.
   *
   * @return the {@link LeaderBoardPosition} list.
   */
  public List<LeaderBoardPosition> getLeaderBoard() {
    final ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(httpUtils.get(CONTEXT_LEADERBOARD),
              objectMapper.getTypeFactory().constructCollectionType(List.class, LeaderBoardPosition.class));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves the User from the system.
   *
   * @param userId the user id.
   * @return the {@link User} returned from the system.
   */
  public User getUser(final long userId) {
    try {
      return new ObjectMapper()
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
          .readValue(httpUtils.get(CONTEXT_USERS + userId), User.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
