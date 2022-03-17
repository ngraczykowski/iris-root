package com.silenteight.connector.ftcc.callback.response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
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
  }
}
