package microservices.book.testutils.beans;

public class AttemptResponse {

  private long id;
  private User user;
  private boolean correct;

  public long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }

  public boolean isCorrect() {
    return correct;
  }

}
