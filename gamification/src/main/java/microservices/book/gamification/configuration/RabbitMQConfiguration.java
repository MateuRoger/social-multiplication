package microservices.book.gamification.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

/**
 * Configures RabbitMQ to use events in our application.
 */
@Configuration
public class RabbitMQConfiguration implements RabbitListenerConfigurer {

  /**
   * Creates a the {@link TopicExchange} Bean.
   *
   * @param exchangeName the exchange name
   * @return the {@link TopicExchange} Bean
   */
  @Bean
  public TopicExchange multiplicationExchange(@Value("${multiplication.exchange}") final String exchangeName) {
    return new TopicExchange(exchangeName);
  }

  /**
   * Creates the {@link Queue} Bean.
   *
   * @param queueName the queue name.
   * @return the {@link Queue} Bean.
   */
  @Bean
  public Queue gamificationMultiplicationQueue(@Value("${multiplication.queue}") final String queueName) {
    return new Queue(queueName, true);
  }

  /**
   * Creates the {@link Binding} Bean.
   *
   * @return the {@link Binding} Bean.
   */
  @Bean
  Binding binding(final Queue queue, final TopicExchange exchange,
      @Value("${multiplication.anything.routing-key}") final String routingKey) {
    return BindingBuilder.bind(queue).to(exchange).with(routingKey);
  }

  /**
   * Creates the {@link MappingJackson2MessageConverter} Bean.
   *
   * @return the {@link MappingJackson2MessageConverter} Bean.
   */
  @Bean
  public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
    return new MappingJackson2MessageConverter();
  }

  /**
   * Create the {@link DefaultMessageHandlerMethodFactory} Bean.
   *
   * @return the {@link DefaultMessageHandlerMethodFactory} Bean.
   */
  @Bean
  public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
    final DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
    factory.setMessageConverter(consumerJackson2MessageConverter());
    return factory;
  }

  @Override
  public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
    registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
  }
}