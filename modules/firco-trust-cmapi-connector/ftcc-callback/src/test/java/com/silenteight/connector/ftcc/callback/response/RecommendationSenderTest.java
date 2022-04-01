package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.exception.NonRecoverableCallbackException;
import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.dto.output.ClientRequestDto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(SpringExtension.class)
class RecommendationSenderTest {

  @Mock
  private RestTemplate restTemplate;
  @Mock
  private ClientRequestDto clientRequestDtoMock;

  private static final String ENDPOINT = "http://localhost:8080/dummy";

  @DisplayName("When process with 200")
  @Test
  void whenSendIsOk() {
    var recommendationSender = new RecommendationSender(restTemplate, ENDPOINT);
    var mockAck = mock(AckDto.class);
    when(restTemplate.postForEntity(ENDPOINT, clientRequestDtoMock, AckDto.class))
        .thenReturn(new ResponseEntity<>(mockAck, OK));
    Assertions.assertDoesNotThrow(() -> recommendationSender.send(clientRequestDtoMock));
  }

  @DisplayName("When status grater than 400 should throw HttpServerErrorException")
  @Test
  void whenHttpStatusIsGraterThen400_shouldThrowException() {
    var recommendationSender = new RecommendationSender(restTemplate, ENDPOINT);
    var mockAck = mock(AckDto.class);
    when(restTemplate.postForEntity(ENDPOINT, clientRequestDtoMock, AckDto.class))
        .thenReturn(new ResponseEntity<>(mockAck, INTERNAL_SERVER_ERROR));
    Assertions.assertThrows(
        NonRecoverableCallbackException.class,
        () -> recommendationSender.send(clientRequestDtoMock));
  }
}
