package microservices.book.gateway.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RibbonConfiguration {

  @Bean
  public IPing ribbonPing() {

    String pingPath = "/actuator/health";
    IPing ping = new PingUrl(false, pingPath);
    log.debug("Configuring ping URI to [{}]", pingPath);
    return ping;
  }

  @Bean
  public IRule loadBalancingRule() {
    return new AvailabilityFilteringRule();
  }
}
