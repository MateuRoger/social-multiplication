package microservices.book.gamification.business.badge;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import microservices.book.gamification.domain.Badge;

/**
 * Is the executor for the {@link Badge} operations.
 */
public class BadgeOperationExecutor {

  private final Queue<BadgeOperation> operationQueue = new ArrayDeque<>();

  /**
   * Adds a {@code badgeOperation} to be executed.
   *
   * @param badgeOperation the {@code badgeOperation} to be executed.
   */
  public void addBadgeOperation(final BadgeOperation badgeOperation) {
    operationQueue.add(badgeOperation);
  }

  /**
   * Executes all the enqueued operations.
   *
   * @return a list of {@link Badge} processed.
   */
  public List<Badge> executeAllOperations() {
    final List<Badge> newBadges = new ArrayList<>();
    operationQueue.forEach(
        badgeOperation -> badgeOperation.execute().ifPresent(newBadges::add));

    return newBadges;
  }

}
