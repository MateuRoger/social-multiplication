package microservices.book.gamification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class GamificationApplication {
  /**
   * The main method of GamificationApplication
   *
   * @param args the args.
   */
  public static void main(final String[] args) {
    SpringApplication.run(GamificationApplication.class, args);
  }

}
