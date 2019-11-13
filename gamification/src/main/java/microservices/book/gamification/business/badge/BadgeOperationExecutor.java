package microservices.book.gamification.business.badge;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
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
   * @return a set of {@link Badge} processed.
   */
  public Set<Badge> executeAllOperations() {
    final Set<Badge> newBadges = new HashSet<>();
    operationQueue.forEach(
        badgeOperation -> badgeOperation.execute().ifPresent(newBadges::add));

    return newBadges;
  }

}
