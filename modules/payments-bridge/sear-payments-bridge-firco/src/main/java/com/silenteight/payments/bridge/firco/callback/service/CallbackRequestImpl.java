package com.silenteight.payments.bridge.firco.callback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.AckDto;
import com.silenteight.payments.bridge.common.dto.output.ClientRequestDto;
import com.silenteight.payments.bridge.firco.callback.model.NonRecoverableCallbackException;
import com.silenteight.payments.bridge.firco.callback.model.RecoverableCallbackException;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
class CallbackRequestImpl implements CallbackRequest {

  private final String endpoint;
  private final RestTemplate restTemplate;
  private final ClientRequestDto clientRequestDto;

  @Timed("ProxyBridgeClient.send")
  @Override
  public void execute() {
    try {
      ResponseEntity<AckDto> responseEntity =
          restTemplate.postForEntity(endpoint, clientRequestDto, AckDto.class);

      log.debug("The response from [{}] is: {}", endpoint, responseEntity);

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
