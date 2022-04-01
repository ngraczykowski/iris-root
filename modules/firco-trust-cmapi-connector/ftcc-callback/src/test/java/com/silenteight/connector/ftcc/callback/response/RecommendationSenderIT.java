package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(classes = CallbackTestConfiguration.class)
@AutoConfigureWebClient
class RecommendationSenderIT {

  @Autowired
  private RecommendationSender classUnderTest;

  private MockRestServiceServer mockServer;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private RecommendationSenderProperties recommendationSenderProperties;

  @BeforeEach
  public void init() {
    mockServer = MockRestServiceServer.createServer(restTemplate);
  }

  @Test
  void whenStatus200ResponseIsCorrectlyParsed() throws URISyntaxException, JsonProcessingException {
    mockServer.expect(
            once(),
            requestTo(new URI(recommendationSenderProperties.getEndpoint())))
        .andExpect(method(POST))
        .andRespond(withStatus(OK)
            .contentType(APPLICATION_JSON)
            .body(INSTANCE.objectMapper().writeValueAsString(AckDto.ok()))
        );
    classUnderTest.send(new ClientRequestDto());
  }
}
