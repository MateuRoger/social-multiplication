package microservices.book.multiplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
class SocialMultiplicationApplication {

  /**
   * The main method of SocialMultiplicationApplication
   *
   * @param args the args.
   */
  public static void main(final String[] args) {
    SpringApplication.run(SocialMultiplicationApplication.class, args);
  }

}
