package microservices.book.gateway.fallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

/**
 * Represents a {@link ClientHttpResponse} when is not possible to reach to any service
 */
public class GatewayClientResponse implements ClientHttpResponse {

  private HttpStatus status;
  private String message;

  GatewayClientResponse(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public HttpStatus getStatusCode() throws IOException {
    return status;
  }

  @Override
  public int getRawStatusCode() throws IOException {
    return status.value();
  }

  @Override
  public String getStatusText() throws IOException {
    return status.getReasonPhrase();
  }

  @Override
  public void close() {
  }

  @Override
  public InputStream getBody() throws IOException {
    return new ByteArrayInputStream(message.getBytes());
  }

  @Override
  public HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccessControlAllowCredentials(true);
    headers.setAccessControlAllowOrigin("*");

    return headers;
  }

}