package microservices.book.testutils.http;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

public class ApplicationHttpUtils {

  private final String baseUrl;

  public ApplicationHttpUtils(final String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String post(final String context, final String body) {
    try {
      final HttpResponse response = Request.Post(baseUrl + context)
          .bodyString(body, ContentType.APPLICATION_JSON)
          .execute().returnResponse();
      assertIs200(response);
      return EntityUtils.toString(response.getEntity());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String get(String context) {
    try {
      final HttpResponse response = Request.Get(baseUrl + context).execute().returnResponse();
      assertIs200(response);
      return EntityUtils.toString(response.getEntity());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private void assertIs200(final HttpResponse response) {
    assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);
  }
}
