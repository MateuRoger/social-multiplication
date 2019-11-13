package microservices.book.gamification.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configures the REST client in our application
 */
@Configuration
class RestClientConfiguration {

  /**
   * Creates the {@link RestTemplate} Bean.
   *
   * @param builder the {@link RestTemplateBuilder} used to create the {@link RestTemplate} Bean.
   * @return the {@link RestTemplate} Bean.
   */
  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    return builder.build();
  }
}
