package com.silenteight.serp.governance.qa.send;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.serp.governance.qa.send.amqp.AlertMessageGateway;
import com.silenteight.serp.governance.qa.send.dto.AlertDto;

import com.google.protobuf.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static com.silenteight.serp.governance.qa.AlertFixture.generateAlertName;
import static com.silenteight.serp.governance.qa.DecisionFixture.COMMENT_OK;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.PASSED;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendAlertMessageUseCaseTest {

  @Mock
  private AlertMessageGateway alertMessageGateway;

  private SendAlertMessageUseCase underTest;

  @BeforeEach
  void setUp() {
    underTest = new SendAlertMessageConfiguration().sendAlertMessageUseCase(alertMessageGateway);
  }

  @Test
  void activateShouldSendProductionDataIndexRequestMessageWithOneAlert() {
    //given
    ArgumentCaptor<ProductionDataIndexRequest> messageCaptor = ArgumentCaptor
        .forClass(ProductionDataIndexRequest.class);
    AlertDto alertDto = getAlertDto();
    Map<String, Value> expectedPayload = new HashMap<>();
    expectedPayload.put("qa.level-0.state", getValueFromString(PASSED.toString()));
    expectedPayload.put("qa.level-0.comment",getValueFromString(COMMENT_OK));
    SendAlertMessageCommand sendAlertMessageCommand = SendAlertMessageCommand.of(of(alertDto));
    //when
    underTest.activate(sendAlertMessageCommand);
    //then
    verify(alertMessageGateway, times(1)).send(messageCaptor.capture());
    assertThat(messageCaptor.getValue().getAlertsCount()).isEqualTo(1);
    assertThat(messageCaptor.getValue().getRequestId()).isEmpty();
    assertThat(messageCaptor.getValue().getAlerts(0).getName())
        .isEqualTo(alertDto.getAlertName());
    assertThat(messageCaptor.getValue().getAlerts(0).getPayload().getFieldsMap())
        .isEqualTo(expectedPayload);
  }

  private AlertDto getAlertDto() {
    return AlertDto.builder()
        .alertName(generateAlertName())
        .level(ANALYSIS)
        .state(PASSED)
        .comment(COMMENT_OK)
        .build();
  }

  private Value getValueFromString(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }
}