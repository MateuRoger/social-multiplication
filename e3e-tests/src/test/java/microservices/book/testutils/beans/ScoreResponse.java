package microservices.book.testutils.beans;

public class ScoreResponse {

  private long userId;
  private int score;

  public ScoreResponse() {
  }

  public ScoreResponse(int score) {
    this.score = score;
  }

  public int getScore() {
    return score;
  }

  public long getUserId() {
    return userId;
  }
}
