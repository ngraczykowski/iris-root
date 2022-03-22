package com.silenteight.connector.ftcc.callback.response;

import com.silenteight.connector.ftcc.callback.decision.DestinationStatus;
import com.silenteight.connector.ftcc.callback.decision.MapStatusUseCase;
import com.silenteight.connector.ftcc.callback.response.domain.MessageEntity;
import com.silenteight.connector.ftcc.callback.response.domain.NextStatusEntity;
import com.silenteight.connector.ftcc.callback.response.domain.StatusEntity;
import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto;
import com.silenteight.recommendation.api.library.v1.AlertOut;
import com.silenteight.recommendation.api.library.v1.RecommendationOut;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith({ SpringExtension.class })
class ResponseCreatorTest {

  @Mock
  private MapStatusUseCase mapStatusUseCase;

  @DisplayName("Minimal Data to create ClientRequestDto for callback")
  @Test
  void dummyCreateClientRequestDto() {
    RecommendationSenderProperties properties = new RecommendationSenderProperties();
    properties.setLogin("login");
    properties.setPassword("password");
    var ds = DestinationStatus.builder()
        .status(new StatusInfoDto("12", "NAME", "CODE", "CHECKSUM"))
        .valid(true)
        .build();
    when(mapStatusUseCase.mapStatus(any())).thenReturn(ds);

    ResponseCreator responseCreator = new ResponseCreator(mapStatusUseCase, properties);

    var message = MessageEntity.builder()
        .messageID("MESSAGEID;)")
        .unit("UNIT!")
        .currentStatus(StatusEntity.builder()
            .id("CurrentStatus ID")
            .checksum("CurrentStatus checksum")
            .name("CurrentStatus name")
            .routingCode("CurrentStatus Code")
            .build())
        .nextStatuses(List.of(NextStatusEntity.builder()
            .status(StatusEntity.builder()
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
        Assertions.assertDoesNotThrow(() -> responseCreator.create(message, recommendation));
    Assertions.assertNotNull(clientRequestDto.getBody());
  }
}
