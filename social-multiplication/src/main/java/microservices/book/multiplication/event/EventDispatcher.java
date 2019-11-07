package microservices.book.multiplication.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Handles the communication with the Event Bus.
 */
@Component
public class EventDispatcher {

  private final RabbitTemplate rabbitTemplate;

  // The exchange to use to send anything related to Multiplication
  private final String multiplicationExchange;

  // The routing key to use to send this particular event
  private final String multiplicationSolvedRoutingKey;

  /**
   * Parametrized constructor
   *
   * @param rabbitTemplate                 the {@link RabbitTemplate}
   * @param multiplicationExchange         the multiplication exchange
   * @param multiplicationSolvedRoutingKey the multiplication solved routing key
   */
  @Autowired
  EventDispatcher(final RabbitTemplate rabbitTemplate,
      @Value("${multiplication.exchange}") final String multiplicationExchange,
      @Value("${multiplication.solved.key}") final String multiplicationSolvedRoutingKey) {
    this.rabbitTemplate = rabbitTemplate;
    this.multiplicationExchange = multiplicationExchange;
    this.multiplicationSolvedRoutingKey = multiplicationSolvedRoutingKey;
  }

  /**
   * Sends the {@link MultiplicationSolvedEvent} to its subscribers.
   *
   * @param multiplicationSolvedEvent the given {@link MultiplicationSolvedEvent}
   */
  public void send(final MultiplicationSolvedEvent multiplicationSolvedEvent) {
    rabbitTemplate.convertAndSend(multiplicationExchange,
        multiplicationSolvedRoutingKey,
        multiplicationSolvedEvent);
  }

}
