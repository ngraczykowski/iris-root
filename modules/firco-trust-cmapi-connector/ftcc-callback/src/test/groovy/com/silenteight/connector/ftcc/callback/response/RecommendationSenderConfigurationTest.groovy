package com.silenteight.connector.ftcc.callback.response

import com.silenteight.connector.ftcc.common.database.partition.PartitionCreator
import com.silenteight.connector.ftcc.request.details.MessageDetailsQuery

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import java.time.Duration

import static java.time.temporal.ChronoUnit.SECONDS
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static org.springframework.http.HttpStatus.OK

@SpringBootTest(classes = [ ResponseConfiguration, ResponseTestConfiguration ])
@AutoConfigureWebClient
class RecommendationSenderConfigurationTest extends Specification {

  @Autowired
  private RecommendationSenderProperties recommendationSenderProperties

  @Autowired
  private RestTemplate restTemplate

  @SpringBean
  private MessageDetailsQuery messageDetailsQuery = Mock()

  @SpringBean
  private CallbackRequestRepository callbackRequestRepository = Mock()

  @SpringBean
  private PartitionCreator partitionCreator = Mock()

  def "RecommendationSender properties are read correctly"() {
    expect:
    recommendationSenderProperties.getConnectionTimeout() == Duration.of(10, SECONDS)
    recommendationSenderProperties.getReadTimeout() == Duration.of(10, SECONDS)
    recommendationSenderProperties.getLogin() == "test"
    recommendationSenderProperties.getPassword() == "test"
    recommendationSenderProperties.getEndpoint() == "https://client.badssl.com"
  }

  def "RestTemplate is configured for mTLS when keystorePath is provided"() {
    when:
    ResponseEntity<String> response =
        restTemplate.getForEntity(recommendationSenderProperties.getEndpoint(), String.class)
    then:
    response.getStatusCode() is OK
  }
}

@SpringBootTest(classes = [ ResponseConfiguration, ResponseTestConfiguration ],
    properties = "ftcc.cmapi.callback.keystorePath:")
@AutoConfigureWebClient
class RecommendationSenderConfigurationWithoutKeystorePathTest extends Specification {

  @Autowired
  private RecommendationSenderProperties recommendationSenderProperties

  @Autowired
  private RestTemplate restTemplate

  @SpringBean
  private MessageDetailsQuery messageDetailsQuery = Mock()

  @SpringBean
  private CallbackRequestRepository callbackRequestRepository = Mock()

  @SpringBean
  private PartitionCreator partitionCreator = Mock()

  def "RecommendationSender properties are read correctly when keystorePath is not provided"() {
    expect:
    recommendationSenderProperties.getConnectionTimeout() == Duration.of(10, SECONDS)
    recommendationSenderProperties.getReadTimeout() == Duration.of(10, SECONDS)
    recommendationSenderProperties.getLogin() == "test"
    recommendationSenderProperties.getPassword() == "test"
    recommendationSenderProperties.getEndpoint() == "https://client.badssl.com"
    recommendationSenderProperties.getKeystorePassword() == "badssl.com"
    recommendationSenderProperties.getKeystorePath() == ""
  }

  def "RestTemplate is not configured for mTLS when keystorePath is not provided"() {
    when:
    restTemplate.getForEntity(recommendationSenderProperties.getEndpoint(), String.class)
    then:
    def e = thrown(HttpClientErrorException)
    e.getStatusCode() == BAD_REQUEST
  }
}
