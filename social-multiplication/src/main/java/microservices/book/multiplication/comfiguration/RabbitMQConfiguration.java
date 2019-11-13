package microservices.book.multiplication.comfiguration;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMQConfiguration {

  /**
   * Creates the {@link TopicExchange} Bean.
   *
   * @param exchangeName the exchange name
   * @return the {@link TopicExchange} Bean.
   */
  @Bean
  public TopicExchange multiplicationExchange(
      @Value("${multiplication.exchange}") final String exchangeName) {
    return new TopicExchange(exchangeName);
  }

  /**
   * Creates the {@link RabbitTemplate} Bean.
   *
   * @param connectionFactory the {@link ConnectionFactory}.
   * @return the {@link RabbitTemplate} Bean.
   */
  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
    return rabbitTemplate;
  }

  /**
   * Creates the {@link Jackson2JsonMessageConverter} Bean.
   *
   * @return the {@link Jackson2JsonMessageConverter} Bean.
   */
  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

}
