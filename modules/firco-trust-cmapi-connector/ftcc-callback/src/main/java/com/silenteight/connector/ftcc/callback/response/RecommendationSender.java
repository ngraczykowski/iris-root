package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.callback.exception.RecoverableCallbackException;
import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
class RecommendationSender {

  private final RestTemplate restTemplate;
  private final String endpoint;
  private final CallbackRequestService callbackRequestService;

  public void send(String batchName, ClientRequestDto clientRequestDto) {
    try {
      var responseEntity = restTemplate.postForEntity(endpoint, clientRequestDto, AckDto.class);

      if (responseEntity.getStatusCodeValue() < 400) {
        saveCallback(batchName, clientRequestDto, responseEntity);
        log.debug(
            "Sent the decision for batch={} to {} and received the response {}", batchName,
            endpoint, responseEntity.getStatusCode());
      } else {
        saveCallback(batchName, clientRequestDto, responseEntity);
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

  private void saveCallback(
      String batchName, ClientRequestDto clientRequestDto, ResponseEntity<AckDto> responseEntity) {
    callbackRequestService.save(batchName, clientRequestDto, endpoint, responseEntity.getBody(),
        responseEntity.getStatusCodeValue());
  }

  private static void logException(String endpoint, Exception exception) {
    log.warn(
        "Unable to send the decision to [{}] due to the exception [{}: {}]",
        endpoint, exception.getClass().getName(), exception.getLocalizedMessage(), exception);
  }

  private static RuntimeException mapToException(HttpServerErrorException exception) {
    switch (exception.getStatusCode()) {
      //      4XX
      case REQUEST_TIMEOUT:
      case TOO_MANY_REQUESTS:
        //        5XX
      case BAD_GATEWAY:
      case SERVICE_UNAVAILABLE:
      case GATEWAY_TIMEOUT:
        return new RecoverableCallbackException(exception);
      default:
        return new NonRecoverableCallbackException(exception);
    }
  }
}
