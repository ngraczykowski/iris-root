package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.newdecision.DecisionMapperUseCase;
import com.silenteight.connector.ftcc.callback.newdecision.DestinationStatus;
import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;
import com.silenteight.connector.ftcc.request.details.dto.MessageDetailsDto;
import com.silenteight.connector.ftcc.request.details.dto.NextStatusDto;
import com.silenteight.connector.ftcc.request.details.dto.StatusDto;
import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith({ SpringExtension.class })
class ResponseCreatorTest {

  @Mock
  private DecisionMapperUseCase decisionMapperUseCase;

  @DisplayName("Minimal Data to create ClientRequestDto for callback")
  @Test
  void dummyCreateClientRequestDto() {
    RecommendationSenderProperties properties =
        new RecommendationSenderProperties("", "login", "password",
            Duration.ofSeconds(10), Duration.ofSeconds(10), "/etc/default/key.pkcs", "password");
    var ds = DestinationStatus.builder()
        .status(StatusInfoDto.builder()
            .id("12")
            .name("NAME")
            .routingCode("CODE")
            .checksum("CHECKSUM")
            .build())
        .valid(true)
        .build();
    when(decisionMapperUseCase.mapStatus(any())).thenReturn(ds);

    ResponseCreator responseCreator = new ResponseCreator(decisionMapperUseCase, properties);

    var message = MessageDetailsDto.builder()
        .messageID("MESSAGEID;)")
        .businessUnit("")
        .systemID("SYSTEMID")
        .unit("UNIT!")
        .currentStatus(StatusDto.builder()
            .id("CurrentStatus ID")
            .checksum("CurrentStatus checksum")
            .name("CurrentStatus name")
            .routingCode("CurrentStatus Code")
            .build())
        .nextStatuses(List.of(NextStatusDto.builder()
            .status(StatusDto.builder()
                .id("Status ID")
                .checksum("Status checksum")
                .name("Status name")
                .routingCode("Status Code")
                .build())
            .build()))
        .build();

    var recommendation = RecommendationOut.builder()
        .recommendationComment("Comment")
        .recommendedAction("PTP")
        .alert(AlertOut.builder().id("messages/" + UUID.randomUUID()).build())
        .build();

    var clientRequestDto =
        Assertions.assertDoesNotThrow(() -> responseCreator.build(
            List.of(responseCreator.buildMessageDto(message, recommendation))));
    Assertions.assertNotNull(clientRequestDto.getBody());
  }
}
