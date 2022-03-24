package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.callback.exception.RecoverableCallbackException;
import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@EnableConfigurationProperties(RecommendationSenderProperties.class)
@RequiredArgsConstructor
@Service
@Slf4j
class RecommendationSender {

  private final RestTemplate restTemplate;
  private final RecommendationSenderProperties properties;

  public void send(ClientRequestDto clientRequestDto) {
    var endpoint = properties.getEndpoint();
    try {
      var responseEntity = restTemplate.postForEntity(endpoint, clientRequestDto, AckDto.class);

      if (responseEntity.getStatusCodeValue() < 400) {
        if (log.isDebugEnabled()) {
          log.debug("Sent the decision to [{}] and received the response [{}]",
              endpoint, responseEntity.getStatusCode());
        }
      } else {
        log.warn("Received an error code [{}] when sending the decision for the alert to [{}]",
            responseEntity.getStatusCode(), endpoint);
        throw new HttpServerErrorException(responseEntity.getStatusCode());
      }

    } catch (HttpServerErrorException exception) {
      logException(endpoint, exception);
      throw mapToException(exception);
    } catch (ResourceAccessException exception) {
      logException(endpoint, exception);
      throw new RecoverableCallbackException(exception);
    } catch (RestClientException exception) {
      logException(endpoint, exception);
      throw new NonRecoverableCallbackException(exception);
    }
  }

  private static void logException(String endpoint, Exception exception) {
    log.warn(
        "Unable to send the decision to [{}] due to the exception [{}: {}]",
        endpoint, exception.getClass().getName(), exception.getLocalizedMessage());
  }

  private static RuntimeException mapToException(HttpServerErrorException exception) {
    switch (exception.getStatusCode()) {
      case UNAUTHORIZED:
      case FORBIDDEN:
      case REQUEST_TIMEOUT:
      case TOO_MANY_REQUESTS:
      case BAD_GATEWAY:
      case SERVICE_UNAVAILABLE:
      case GATEWAY_TIMEOUT:
        return new RecoverableCallbackException(exception);
      default:
        return new NonRecoverableCallbackException(exception);
    }
  }
}
