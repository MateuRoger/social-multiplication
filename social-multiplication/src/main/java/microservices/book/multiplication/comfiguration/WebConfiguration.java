package microservices.book.multiplication.comfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORES configuration
 */
@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

  /**
   * Enables Cross-Origin Resource Sharing (CORS) More info: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/cors.html
   *
   * @param registry the {@link CorsRegistry}
   */
  @Override
  public void addCorsMappings(final CorsRegistry registry) {
    registry
        .addMapping("/**");
  }

}
