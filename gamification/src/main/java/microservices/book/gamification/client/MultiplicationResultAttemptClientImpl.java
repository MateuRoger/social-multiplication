package microservices.book.gamification.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.ribbon.proxy.annotation.Hystrix;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.client.dto.MultiplicationResultAttempt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This implementation of MultiplicationResultAttemptClient interface connects to the Multiplication microservice via
 * REST.
 */
@Slf4j
@Component
public class MultiplicationResultAttemptClientImpl implements MultiplicationResultAttemptClient {

  private final RestTemplate restTemplate;
  private final String multiplicationHost;

  /**
   * Constructor
   *
   * @param restTemplate       the {@link RestTemplate}
   * @param multiplicationHost the multiplication microservice HOST
   */
  @Autowired
  public MultiplicationResultAttemptClientImpl(final RestTemplate restTemplate,
      @Value("${multiplicationHost}") final String multiplicationHost) {
    this.restTemplate = restTemplate;
    this.multiplicationHost = multiplicationHost;
  }

  @HystrixCommand(fallbackMethod = "defaultResult")
  @Override
  public MultiplicationResultAttempt retrieveMultiplicationResultAttemptById(final Long multiplicationResultAttemptId) {
    log.info("Retrieving the multiplication result attempt by its id = {}", multiplicationResultAttemptId);
    return restTemplate.getForObject(
        multiplicationHost + "/results/" + multiplicationResultAttemptId,
        MultiplicationResultAttempt.class);
  }

  private MultiplicationResultAttempt defaultResult(final Long multiplicationResultAttemptId) {
    log.info("The multiplication service does not response. Procedding to return a default result");
    return new MultiplicationResultAttempt("fakeAlias", 10, 10, 100, true);
  }
}
