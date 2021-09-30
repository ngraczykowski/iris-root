package com.silenteight.payments.bridge.firco.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.AckDto;
import com.silenteight.payments.bridge.common.dto.output.ClientRequestDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
class CallbackRequestImpl implements CallbackRequest {

  private final String endpoint;
  private final RestTemplate restTemplate;
  private final ClientRequestDto clientRequestDto;

  @Timed("ProxyBridgeClient.send")
  @Override
  public void invoke() {
    try {
      ResponseEntity<AckDto> responseEntity =
          restTemplate.postForEntity(endpoint, clientRequestDto, AckDto.class);

      log.debug("The response from [{}] is: {}", endpoint, responseEntity);

      if (responseEntity.getStatusCodeValue() < 400) {
        if (log.isDebugEnabled())
          log.debug("I sent the decision to [{}] and received the response [{}]",
              endpoint, responseEntity.getStatusCode());
      } else {
        log.warn("I received the error code [{}] when sending the decision for the alert to [{}]",
            responseEntity.getStatusCode(), endpoint);
        throw new HttpServerErrorException(responseEntity.getStatusCode());
      }
    } catch (ResourceAccessException e) {
      log.warn(
          "I was unable to send the decision to [{}] due to the exception [{}: {}]",
          endpoint, e.getClass().getName(), e.getLocalizedMessage());
      throw e;
    }
  }
}
