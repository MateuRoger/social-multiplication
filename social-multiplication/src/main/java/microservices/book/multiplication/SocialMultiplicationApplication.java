package microservices.book.multiplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@SpringBootApplication
class SocialMultiplicationApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocialMultiplicationApplication.class, args);
  }

}
